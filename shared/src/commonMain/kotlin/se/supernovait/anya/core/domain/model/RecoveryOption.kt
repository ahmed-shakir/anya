package se.supernovait.anya.core.domain.model

/**
 * Recovery strategies when initialization fails.
 * Allows users meaningful choices to recover from errors.
 */
enum class RecoveryOption {
    /**
     * Retry initialization without any changes.
     * Used for transient errors like network timeouts.
     */
    RETRY,

    /**
     * Clear all app data (database, preferences, cache) and retry.
     * Used for corrupted data or database errors.
     */
    CLEAR_APP_DATA,

    /**
     * Skip network requirement and use offline/cached data.
     * Used for network errors when app might work offline.
     */
    OFFLINE_MODE,

    /**
     * Reset preferences to defaults.
     * Used for preferences/configuration errors.
     */
    RESET_PREFERENCES
}