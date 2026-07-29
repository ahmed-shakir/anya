package se.supernovait.anya.core.domain.network

import se.supernovait.anya.core.domain.util.currentTimeMilliseconds
import kotlin.time.Clock

/**
 * Represents the current network connectivity state and validation.
 *
 * @property type The overall status type: OFFLINE, ONLINE, or RESTRICTED.
 * @property networkType The type of the active network connection.
 * @property isConnected Whether the device has an active network connection (OS-level).
 * @property isAllowed Whether the current network type is allowed by the policy.
 * @property isReachable Whether the connection has passed validation checks (TCP, HTTP).
 *           True only if the device can reach external hosts.
 *           False does not necessarily mean offline — could be behind a captive portal.
 * @property isDirect Whether the connection is direct (not through VPN or proxy).
 * @property isMetered Whether the connection is metered (data-limited).
 * @property isCaptivePortal Whether the device is behind a captive portal.
 * @property issue Specific connectivity issue if any.
 * @property timestamp Time when this status was recorded.
 *
 * State Combinations:
 * - OFFLINE: isConnected=false (network unavailable)
 * - ONLINE: isConnected=true, isAllowed=true (connected and policy-compliant)
 * - RESTRICTED: isConnected=true, isAllowed=false (connected but policy-violating)
 *
 * Example scenarios:
 * ```
 * // Good: Wi-Fi, policy-allowed, reachable
 * NetworkStatus(
 *     type = NetworkStatusType.ONLINE,
 *     networkType = NetworkType.WIFI,
 *     isConnected = true,
 *     isAllowed = true,
 *     isReachable = true,
 *     isDirect = true
 * )
 *
 * // Bad: Captive portal (connected but can't reach internet)
 * NetworkStatus(
 *     type = NetworkStatusType.ONLINE,
 *     networkType = NetworkType.WIFI,
 *     isConnected = true,
 *     isAllowed = true,
 *     isReachable = false,
 *     isDirect = false,
 *     isCaptivePortal = true,
 *     issue = NetworkIssue.CAPTIVE_PORTAL
 * )
 * ```
 */
data class NetworkStatus(
    val type: NetworkStatusType,
    val networkType: NetworkType,
    val isConnected: Boolean,
    val isAllowed: Boolean,
    val isReachable: Boolean = false,
    val isDirect: Boolean = true,
    val isMetered: Boolean = false,
    val isCaptivePortal: Boolean = false,
    val issue: NetworkIssue = NetworkIssue.NONE,
    val timestamp: Long = Clock.currentTimeMilliseconds()
) {
    /**
     * Convenience property: true if network is available, reachable (validated TCP), and policy-compliant.
     */
    val isUsable: Boolean
        get() = isConnected && isReachable && isAllowed

    /**
     * Convenience property: true if the connection is suitable for heavy operations.
     * Requires: connected, reachable (validated TCP), allowed, and not metered.
     */
    val isSuitableForHeavyOps: Boolean
        get() = isUsable && !isMetered

    /**
     * Convenience property: true if connection is safe for sensitive operations (payment, auth etc.).
     * Requires: connected, reachable (validated TCP), allowed, and direct (no VPN/proxy).
     */
    val isSafeForSensitiveOps: Boolean
        get() = isUsable && isDirect
}
