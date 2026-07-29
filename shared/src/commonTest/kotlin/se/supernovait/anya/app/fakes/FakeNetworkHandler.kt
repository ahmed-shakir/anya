package se.supernovait.anya.app.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.domain.network.NetworkPolicy
import se.supernovait.anya.core.domain.network.NetworkStatus
import se.supernovait.anya.core.domain.network.NetworkStatusType
import se.supernovait.anya.core.domain.network.NetworkType

class FakeNetworkHandler : NetworkHandler {
    private val defaultStatus = NetworkStatus(
        type = NetworkStatusType.ONLINE,
        networkType = NetworkType.WIFI,
        isConnected = true,
        isAllowed = true,
        isReachable = true
    )
    override val connectivity: Flow<NetworkStatus> = MutableStateFlow(defaultStatus)
    override suspend fun status(): NetworkStatus = defaultStatus
    override suspend fun isConnected(): Boolean = true
    override suspend fun isReachable(): Boolean = true
    override suspend fun isAllowed(): Boolean = true
    override suspend fun isMetered(): Boolean = false
    override suspend fun isVpn(): Boolean = false
    override suspend fun isProxy(): Boolean = false
    override fun policy(): NetworkPolicy = NetworkPolicy.default()
    override fun setPolicy(policy: NetworkPolicy) {}
    override fun getLocalIpAddress(): String = ""
    override fun getGateway(): String = ""
    override fun getSubnetMask(): String = ""
    override fun getDns(): String = ""
    override fun getAddressType(): String = ""
    override fun getDiagnosticsSnapshot(): String = ""
    override fun onAppResumed() {}
    override fun onAppPaused() {}
    override fun cleanup() {}
}
