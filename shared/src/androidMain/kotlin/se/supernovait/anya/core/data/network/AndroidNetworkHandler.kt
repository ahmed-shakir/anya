package se.supernovait.anya.core.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.domain.network.NetworkIssue
import se.supernovait.anya.core.domain.network.NetworkPolicy
import se.supernovait.anya.core.domain.network.NetworkStatus
import se.supernovait.anya.core.domain.network.NetworkStatusType
import se.supernovait.anya.core.domain.network.NetworkType
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration.Companion.milliseconds

/**
 * Android implementation of [NetworkHandler].
 *
 * Uses ConnectivityManager callbacks for real-time OS-level updates
 * and TCP socket binding for validation (detects captive portals, validates Layer 4).
 *
 * Lifecycle:
 * - Callbacks are registered on first collection of [connectivity] flow.
 * - Resources are auto-cleaned on flow cancellation.
 * - Call [cleanup] before app shutdown to guarantee resource release.
 *
 * Thread safety:
 * - All operations are thread-safe via AtomicReference and Mutex.
 * - Callbacks are invoked by the Android system; emission to flow is queue-safe.
 *
 * @param context Android context (used to obtain ConnectivityManager).
 * @param debounceMs Milliseconds to debounce connectivity updates. Defaults to 300ms.
 * @param initialPolicy Initial [NetworkPolicy] (defaults to [NetworkPolicy.default]).
 */
class AndroidNetworkHandler(
    context: Context,
    debounceMs: Long = 300L,
    initialPolicy: NetworkPolicy = NetworkPolicy.default()
) : NetworkHandler {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var currentPolicy = initialPolicy
    private val activeNetwork = AtomicReference<Network?>(null)
    private val isValidated = AtomicReference(false)
    private val isNetworkStatusInitialized = AtomicReference(false)
    private var registeredCallback: ConnectivityManager.NetworkCallback? = null
    private var callbackRegistered = false

    init {
        // Seed initial state immediately
        val network = connectivityManager.activeNetwork
        activeNetwork.set(network)
        if (network != null) {
            isNetworkStatusInitialized.set(true)
        }
    }

    @OptIn(FlowPreview::class)
    override val connectivity: Flow<NetworkStatus> = callbackFlow {
        Log.i(TAG, "Registering network callback")

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.i(TAG, "Network available: ${getNetworkTypeName(network)}")
                activeNetwork.set(network)
                isValidated.set(false)
                trySend(getCurrentStatus())
            }

            override fun onUnavailable() {
                Log.i(TAG, "Network unavailable")
                activeNetwork.set(null)
                isValidated.set(false)
                trySend(getCurrentStatus())
            }

            override fun onLost(network: Network) {
                Log.i(TAG, "Network lost: ${getNetworkTypeName(network)}")
                activeNetwork.compareAndSet(network, null)
                isValidated.set(false)
                trySend(getCurrentStatus())
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                Log.i(TAG, "Network capabilities changed: ${getNetworkTypeName(network)}")
                trySend(getCurrentStatus())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
            registeredCallback = callback
            callbackRegistered = true
            isNetworkStatusInitialized.set(true)

            // Emit current state immediately
            trySend(getCurrentStatus())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register network callback", e)
            close(e)
            return@callbackFlow
        }

        awaitClose {
            try {
                if (callbackRegistered) {
                    connectivityManager.unregisterNetworkCallback(callback)
                    Log.i(TAG, "Network callback unregistered")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to unregister network callback", e)
            }
        }
    }
        .debounce(debounceMs.milliseconds)
        .distinctUntilChanged()

    override suspend fun status(): NetworkStatus = getCurrentStatus()

    override suspend fun isConnected(): Boolean = getCurrentStatus().isConnected

    override suspend fun isReachable(): Boolean = withContext(Dispatchers.IO) {
        val network = activeNetwork.get() ?: return@withContext false
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        
        // Primary check: OS-level validation
        if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true) {
            isValidated.set(true)
            return@withContext true
        }

        // Fallback: manual TCP check
        val result = isTcpReachable()
        isValidated.set(result)
        
        return@withContext result
    }

    override suspend fun isAllowed(): Boolean = getCurrentStatus().isAllowed

    override suspend fun isMetered(): Boolean = getCurrentStatus().isMetered

    override suspend fun isVpn(): Boolean = isNetworkVpn()

    override suspend fun isProxy(): Boolean = isNetworkProxy()

    override fun policy(): NetworkPolicy = currentPolicy

    override fun setPolicy(policy: NetworkPolicy) {
        currentPolicy = policy
        Log.i(TAG, "Network policy updated: $policy")
    }

    override fun getLocalIpAddress(): String = runCatching {
        linkProperties()
            ?.linkAddresses
            ?.firstOrNull { it.address is Inet4Address }
            ?.address
            ?.hostAddress
            ?: DEFAULT_ADDRESS
    }.getOrElse { e ->
        Log.e(TAG, "Failed to get local IP address for ${getNetworkTypeName(activeNetwork.get())}", e)
        DEFAULT_ADDRESS
    }

    override fun getGateway(): String = runCatching {
        linkProperties()
            ?.routes
            ?.firstOrNull { it.gateway is Inet4Address && it.gateway.toString() != EMPTY_GATEWAY }
            ?.gateway
            ?.hostAddress
            ?: DEFAULT_ADDRESS
    }.getOrElse { e ->
        Log.e(TAG, "Failed to get gateway for ${getNetworkTypeName(activeNetwork.get())}", e)
        DEFAULT_ADDRESS
    }

    override fun getSubnetMask(): String = runCatching {
        val prefixLength = linkProperties()
            ?.linkAddresses
            ?.firstOrNull { it.address is Inet4Address }
            ?.prefixLength ?: return DEFAULT_ADDRESS

        prefixLengthToSubnetMask(prefixLength)
    }.getOrElse { e ->
        Log.e(TAG, "Failed to get subnet mask for ${getNetworkTypeName(activeNetwork.get())}", e)
        DEFAULT_ADDRESS
    }

    override fun getDns(): String = runCatching {
        linkProperties()
            ?.dnsServers
            ?.firstOrNull { it is Inet4Address }
            ?.hostAddress
            ?: DEFAULT_ADDRESS
    }.getOrElse { e ->
        Log.e(TAG, "Failed to get DNS for ${getNetworkTypeName(activeNetwork.get())}", e)
        DEFAULT_ADDRESS
    }

    override fun getAddressType(): String = runCatching {
        val lp = linkProperties() ?: return "UNKNOWN"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (lp.dhcpServerAddress != null) return "DHCP"
        }

        val network = activeNetwork.get()
        val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }
        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) {
            return "DHCP"
        }

        "UNKNOWN"
    }.getOrElse { e ->
        Log.e(TAG, "Failed to determine address type for ${getNetworkTypeName(activeNetwork.get())}", e)
        "UNKNOWN"
    }

    override fun getDiagnosticsSnapshot(): String = buildString {
        appendLine("=== Network Diagnostics Snapshot ===")
        appendLine("Active Transport: ${getNetworkTypeName(activeNetwork.get())}")
        appendLine("VPN Active      : ${isNetworkVpn()}")
        appendLine("Proxy Active    : ${isNetworkProxy()}")
        appendLine("IPv4 Address    : ${getLocalIpAddress()}")
        appendLine("Default Gateway : ${getGateway()}")
        appendLine("Subnet Mask     : ${getSubnetMask()}")
        appendLine("Primary DNS     : ${getDns()}")
        appendLine("Assignment Type : ${getAddressType()}")
        appendLine("Network Handle  : ${activeNetwork.get()}")
        appendLine("====================================")
    }.also { Log.i(TAG, it) }

    // ────────────────────────────────────────────────────────────────────────────────
    // App Lifecycle
    // ────────────────────────────────────────────────────────────────────────────────

    override fun onAppResumed() {
        // No-op on Android (callbacks are always active)
    }

    override fun onAppPaused() {
        // No-op on Android (callbacks are always active)
    }

    override fun cleanup() {
        try {
            if (callbackRegistered && registeredCallback != null) {
                connectivityManager.unregisterNetworkCallback(registeredCallback!!)
                callbackRegistered = false
                Log.d(TAG, "Network handler cleaned up")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup", e)
        }
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ────────────────────────────────────────────────────────────────────────────────

    private fun getCurrentStatus(): NetworkStatus {
        val network = activeNetwork.get()
        val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }

        val networkType = detectNetworkType(network, capabilities)
        val isConnected = networkType != NetworkType.NONE && capabilities.hasInternetCapabilities()
        val isReachable = isValidated.get() || capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        val isMetered = detectMetered()
        val isCaptivePortal = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL) == true
        val isVpn = isNetworkVpn()
        val isProxy = isNetworkProxy()
        val isDirect = !isVpn && !isProxy && !isCaptivePortal
        val isAllowed = isConnected && currentPolicy.isTypeAllowed(networkType, isVpn)
        
        val issue = when {
            !isConnected -> NetworkIssue.NOT_CONNECTED
            isVpn && currentPolicy.blockVpn -> NetworkIssue.VPN_DETECTED
            isProxy -> NetworkIssue.PROXY_DETECTED 
            !isAllowed -> NetworkIssue.POLICY_VIOLATION
            isCaptivePortal -> NetworkIssue.CAPTIVE_PORTAL
            else -> NetworkIssue.NONE
        }
        
        val statusType = when {
            !isNetworkStatusInitialized.get() -> NetworkStatusType.INITIALIZING
            !isConnected -> NetworkStatusType.OFFLINE
            isConnected && isAllowed -> NetworkStatusType.ONLINE
            else -> NetworkStatusType.RESTRICTED
        }

        val status = NetworkStatus(
            type = statusType,
            networkType = networkType,
            isConnected = isConnected,
            isAllowed = isAllowed,
            isReachable = isReachable,
            isDirect = isDirect,
            isMetered = isMetered,
            isCaptivePortal = isCaptivePortal,
            issue = issue
        )

        Log.d(TAG, "Status: $status")
        return status
    }

    private fun detectNetworkType(network: Network?, capabilities: NetworkCapabilities?): NetworkType {
        if (network == null || capabilities == null) return NetworkType.NONE

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> NetworkType.VPN
            else -> NetworkType.OTHER
        }
    }

    private fun getNetworkTypeName(network: Network?): String {
        if (network == null) return NetworkType.UNKNOWN.name
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.UNKNOWN.name
        return detectNetworkType(network, capabilities).name
    }

    private fun detectMetered(): Boolean {
        return connectivityManager.isActiveNetworkMetered
    }

    private fun isNetworkVpn(): Boolean {
        val network = activeNetwork.get() ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }

    private fun isNetworkProxy(): Boolean {
        return linkProperties()?.httpProxy != null
    }

    private fun prefixLengthToSubnetMask(prefixLength: Int): String {
        if (prefixLength <= 0) return DEFAULT_ADDRESS
        if (prefixLength >= 32) return "255.255.255.255"
        
        val mask = -1 shl (32 - prefixLength)
        return String.format(
            Locale.US,
            "%d.%d.%d.%d",
            (mask shr 24) and 0xFF,
            (mask shr 16) and 0xFF,
            (mask shr 8) and 0xFF,
            mask and 0xFF
        )
    }

    /**
     * Validates connectivity by binding a TCP socket to the active network.
     *
     * This detects:
     * - Captive portals (Wi-Fi login pages)
     * - Dead network connections
     * - Routing issues specific to the active interface
     *
     * Uses network binding to ensure we're testing the right interface
     * (critical when both Wi-Fi and cellular are available).
     *
     * @return True if TCP handshake succeeds, false otherwise.
     */
    private fun isTcpReachable(): Boolean {
        val network = activeNetwork.get() ?: return false

        return try {
            val socket = network.socketFactory.createSocket()
            socket.use {
                val host = InetAddress.getByName(TCP_VALIDATION_HOST)
                it.connect(InetSocketAddress(host, TCP_VALIDATION_PORT), TCP_TIMEOUT_MS)
            }
            Log.d(TAG, "TCP validation successful: $TCP_VALIDATION_HOST:$TCP_VALIDATION_PORT")
            true
        } catch (e: Exception) {
            Log.d(TAG, "TCP validation failed: ${e.javaClass.simpleName} — ${e.message}")
            false
        }
    }

    /**
     * Returns [LinkProperties] for the currently active network, or null.
     * Reads from the cached [activeNetwork] snapshot; falls back to the OS active network.
     */
    private fun linkProperties() =
        (activeNetwork.get() ?: connectivityManager.activeNetwork)
            ?.let { connectivityManager.getLinkProperties(it) }

    companion object {
        private const val TAG: String = "NetworkHandler"
        private const val DEFAULT_ADDRESS = "0.0.0.0"
        private const val EMPTY_GATEWAY = "/0.0.0.0"
        private const val TCP_VALIDATION_HOST = "8.8.8.8" // Google DNS
        private const val TCP_VALIDATION_PORT = 80
        private const val TCP_TIMEOUT_MS = 2000
    }
}

private fun NetworkCapabilities?.hasInternetCapabilities(): Boolean {
    return this?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
