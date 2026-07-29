package se.supernovait.anya.core.domain.network

/**
 * Represents the type of network connection.
 *
 * - WIFI: WiFi connection (typically unmetered)
 * - CELLULAR: Mobile cellular connection (typically metered)
 * - ETHERNET: Wired Ethernet connection
 * - VPN: Virtual Private Network connection
 * - OTHER: Other connection types
 * - NONE: No network connection
 * - UNKNOWN: Unknown network connection
 */
enum class NetworkType {
    WIFI,
    CELLULAR,
    ETHERNET,
    VPN,
    OTHER,
    NONE,
    UNKNOWN;

    /**
     * Returns true if this network type is typically metered.
     * Metered connections have data limits and should be used conservatively.
     */
    val isTypicallyMetered: Boolean
        get() = this == CELLULAR

    /**
     * Returns true if this network type is a direct connection.
     * VPN and OTHER are not considered direct.
     */
    val isDirectConnection: Boolean
        get() = this != VPN && this != OTHER && this != NONE
}
