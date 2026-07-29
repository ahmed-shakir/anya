package se.supernovait.anya.core.domain.network

import kotlinx.coroutines.flow.Flow

/**
 * Interface for monitoring network connectivity with policy enforcement and lifecycle management.
 *
 * Supports:
 * - Real connectivity validation (TCP + OS checks)
 * - VPN detection
 * - Configurable network policies
 * - Proper resource lifecycle
 * - Reactive flow-based updates
 *
 * Platform Implementation:
 * - Android: ConnectivityManager callbacks + TCP socket validation
 * - iOS: NWPathMonitor + network interface detection
 */
interface NetworkHandler {

    /**
     * A flow of [NetworkStatus] updates.
     *
     * Emits on:
     * - Network availability changes
     * - Network type changes
     * - Policy configuration changes
     * - Connection validation state changes
     *
     * Uses `distinctUntilChanged()` to prevent duplicate emissions.
     */
    val connectivity: Flow<NetworkStatus>

    /**
     * Gets the current network status synchronously (cached snapshot).
     *
     * @return The current [NetworkStatus].
     */
    suspend fun status(): NetworkStatus

    /**
     * Checks if the device has an active network connection.
     *
     * This indicates the OS-level link status (e.g., Wi-Fi connected, Cellular active).
     * It does not guarantee that the internet is reachable.
     *
     * @return True if an active network connection exists, false otherwise.
     */
    suspend fun isConnected(): Boolean

    /**
     * Checks if the active network can reach external hosts (TCP socket validation).
     *
     * More robust than OS-level checks alone:
     * - Detects captive portals (Wi-Fi login pages)
     * - Validates Layer 4 connectivity
     * - Bound to the active network interface (respects VPN routing)
     *
     * Should be called periodically for critical operations (payments, uploads).
     *
     * @return True if external connectivity is verified, false otherwise.
     */
    suspend fun isReachable(): Boolean

    /**
     * Checks if the current network connection is allowed by the configured policy.
     *
     * A network can be online but disallowed if:
     * - It's a VPN and VPNs are not allowed
     * - It's cellular and only Wi-Fi is allowed
     * - The connection fails validation checks
     *
     * @return True if the current network satisfies the policy, false otherwise.
     */
    suspend fun isAllowed(): Boolean

    /**
     * Checks if the current network connection is metered (data-limited).
     * Useful for avoiding large downloads on cellular or hotspot connections.
     *
     * @return True if the connection is metered, false otherwise.
     */
    suspend fun isMetered(): Boolean

    /**
     * Checks if the device is currently connected via VPN.
     *
     * @return True if a VPN connection is active.
     */
    suspend fun isVpn(): Boolean

    /**
     * Checks if the device is currently connected via proxy.
     *
     * @return True if a proxy connection is active.
     */
    suspend fun isProxy(): Boolean

    /**
     * Gets the current network policy.
     *
     * @return The [NetworkPolicy] that defines allowed network types.
     */
    fun policy(): NetworkPolicy

    /**
     * Updates the network policy at runtime.
     *
     * Triggers validation of the current connection against the new policy.
     * If the current network becomes disallowed, a new status update is emitted.
     *
     * @param policy The new [NetworkPolicy] to apply.
     */
    fun setPolicy(policy: NetworkPolicy)

    /**
     * The device's local IPv4 address.
     *
     * @return The local IPv4 address string (e.g., "192.168.1.15"), or "0.0.0.0" if unavailable.
     */
    fun getLocalIpAddress(): String

    /**
     * Gets the default gateway IPv4 address for the active network interface.
     *
     * @return The gateway address string, or "0.0.0.0" if unavailable.
     */
    fun getGateway(): String

    /**
     * Gets the subnet mask of the active network interface.
     *
     * Derived from the link address prefix length (e.g., a prefix length of 24 results in "255.255.255.0").
     *
     * @return The subnet mask string, or "0.0.0.0" if unavailable.
     */
    fun getSubnetMask(): String

    /**
     * Gets the primary DNS server address for the active network.
     *
     * @return The DNS server IPv4 address string, or "0.0.0.0" if unavailable.
     */
    fun getDns(): String

    /**
     * Gets the address assignment type for the current connection.
     *
     * @return "DHCP" if assigned via DHCP, "STATIC" if manually configured, or "UNKNOWN" if it cannot be determined.
     */
    fun getAddressType(): String

    /**
     * Assembles a comprehensive diagnostics snapshot of the current network state.
     *
     * Includes transport type, VPN/Proxy status, IP configuration, and interface details.
     * Useful for log dumps, crash reports, and support tickets.
     *
     * @return A multi-line string containing the diagnostic information.
     */
    fun getDiagnosticsSnapshot(): String

    /**
     * Called by the app when it enters the foreground.
     *
     * Platform-specific behavior:
     * - Android: No-op (callbacks are always active)
     * - iOS: Resumes path monitoring
     *
     * Should be called from Activity.onStart() or equivalent.
     */
    fun onAppResumed()

    /**
     * Called by the app when it enters the background.
     *
     * Platform-specific behavior:
     * - Android: No-op (callbacks continue)
     * - iOS: Pauses path monitoring to reduce power consumption
     *
     * Should be called from Activity.onStop() or equivalent.
     */
    fun onAppPaused()

    /**
     * Cleans up resources and stops monitoring.
     *
     * Platform-specific behavior:
     * - Android: Unregisters the network callback
     * - iOS: Stops the path monitor
     *
     * Should be called from Activity.onDestroy() or equivalent.
     * After calling this, [connectivity] will not emit new values.
     */
    fun cleanup()
}