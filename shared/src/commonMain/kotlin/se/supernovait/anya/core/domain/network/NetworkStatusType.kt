package se.supernovait.anya.core.domain.network

/**
 * Represents the overall network status state.
 *
 * - INITIALIZING: Network status is being determined
 * - OFFLINE: No network connection available
 * - ONLINE: Connected with an allowed network type
 * - RESTRICTED: Connected but network type is not allowed by policy
 */
enum class NetworkStatusType {
    INITIALIZING,
    OFFLINE,
    ONLINE,
    RESTRICTED
}
