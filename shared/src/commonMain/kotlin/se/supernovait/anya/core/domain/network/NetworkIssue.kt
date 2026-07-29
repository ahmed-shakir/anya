package se.supernovait.anya.core.domain.network

/**
 * Categorizes specific issues that might affect network connectivity or policy compliance.
 */
enum class NetworkIssue {
    /** No issues detected. Connection is either fully ONLINE or OFFLINE as expected. */
    NONE,

    /** Device is not connected to any network. */
    NOT_CONNECTED,

    /** The current network type (e.g., Cellular) is not allowed by the [NetworkPolicy]. */
    POLICY_VIOLATION,

    /** A VPN connection is active, which might be blocked by [NetworkPolicy]. */
    VPN_DETECTED,

    /** A proxy is configured on the connection, which might be restricted. */
    PROXY_DETECTED,

    /** The connection is behind a captive portal (needs login/authentication). */
    CAPTIVE_PORTAL,

    /** TCP validation failed even though the OS reports being online. */
    VALIDATION_FAILED
}
