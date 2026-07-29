package se.supernovait.anya.core.domain.network

data class NetworkPolicy(
    val allowedTypes: Set<NetworkType>,
    val requireValidation: Boolean = false,
    val blockVpn: Boolean = false
) {
    /**
     * Checks if a given [NetworkType] is allowed by this policy.
     *
     * @param networkType The network type to check.
     * @param isVpn Whether the connection is a VPN.
     * @return True if the network type is in [allowedTypes] and not blocked by VPN rules.
     */
    fun isTypeAllowed(networkType: NetworkType, isVpn: Boolean = false): Boolean {
        if (blockVpn && isVpn) return false
        return networkType in allowedTypes
    }

    companion object {
        /**
         * Default policy: allows all network types, no validation required.
         */
        fun default(): NetworkPolicy = NetworkPolicy(
            allowedTypes = NetworkType.entries.toSet() - NetworkType.NONE,
            requireValidation = false,
            blockVpn = false
        )

        /**
         * Strict policy: Wi-Fi & Cellular only, VPN blocked, validation required.
         *
         * Recommended for:
         * - Payment operations
         * - Sensetive transactions
         */
        fun strict(): NetworkPolicy = NetworkPolicy(
            allowedTypes = setOf(NetworkType.WIFI, NetworkType.CELLULAR),
            requireValidation = true,
            blockVpn = true
        )

        /**
         * Permissive policy: allows all networks except VPN.
         * Suitable for non-sensitive operations like downloading data, syncing metadata.
         */
        fun permissive(): NetworkPolicy = NetworkPolicy(
            allowedTypes = NetworkType.entries.toSet() - NetworkType.NONE - NetworkType.VPN,
            requireValidation = false,
            blockVpn = true
        )
    }
}
