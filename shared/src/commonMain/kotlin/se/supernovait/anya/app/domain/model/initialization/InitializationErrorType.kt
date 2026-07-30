package se.supernovait.anya.app.domain.model.initialization

/**
 * Categorizes initialization errors for proper error screen handling
 */
enum class InitializationErrorType {
    /**
     * Network is unavailable. User can retry or enter offline mode.
     */
    NETWORK,

    /**
     * Database initialization failed. App should terminate.
     */
    DATABASE,

    /**
     * Preferences initialization failed. App should terminate.
     */
    PREFERENCES,

    /**
     * Unknown error during initialization.
     */
    UNKNOWN
}
