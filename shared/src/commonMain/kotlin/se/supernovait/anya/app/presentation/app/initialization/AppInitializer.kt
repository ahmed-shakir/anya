package se.supernovait.anya.app.presentation.app.initialization

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.app.data.local.clearAllTablesKmp
import se.supernovait.anya.app.domain.model.initialization.InitializationErrorType
import se.supernovait.anya.app.domain.model.initialization.InitializationException
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.core.domain.model.RecoveryOption
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.domain.network.NetworkStatusType
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

/**
 * Orchestrates all app initialization tasks that must complete before showing UI.
 * Should be called from MainActivity/MainViewController before App() is displayed.
 *
 * Responsibilities:
 * 1. Verify Database is accessible
 * 2. Verify Preferences/DataStore is accessible
 * 3. Check network status
 * 4. Check authentication and load authenticated user
 * 5. Manage AppInitializationState and AppAuthState
 * 6. Enforce minimum splash screen duration (2.5 seconds)
 */
class AppInitializer : KoinComponent {
    private val database: CatDatabase by inject()
    private val preferences: DataStore<Preferences> by inject()
    private val authManager: AuthenticationManager by inject()
    private val networkHandler: NetworkHandler by inject()

    private val _appInitState = MutableStateFlow<AppInitializationState>(AppInitializationState.Initializing)
    val appInitState = _appInitState.asStateFlow()

    /**
     * Execute all initialization tasks in sequence.
     * Enforces minimum splash screen duration before emitting Success state.
     *
     * Call this once during app startup, before showing the main UI.
     */
    suspend fun initialize() {
        val startTime = Clock.System.now()

        try {
            _appInitState.value = AppInitializationState.Initializing

            // Step 1: Verify Database is accessible
            verifyDatabase()

            // Step 2: Verify Preferences is accessible
            verifyPreferences()

            // Step 3: Check network status
            checkNetworkStatus()

            // Step 4: Check authentication and load user
            checkAuthenticationAndLoadUser()

            // Enforce minimum splash duration for branding/UX
            val elapsedTime = Clock.System.now() - startTime
            if (elapsedTime < MIN_SPLASH_DURATION_MS) {
                delay(MIN_SPLASH_DURATION_MS - elapsedTime)
            }

            // All initialization succeeded
            _appInitState.value = AppInitializationState.Success
        } catch (e: InitializationException) {
            // Known initialization error - categorized with error type
            _appInitState.value = AppInitializationState.Error(
                message = e.message ?: "Unknown initialization error",
                errorType = e.errorType,
                throwable = e
            )
            println("AppInitializer: Initialization failed - ${e.errorType}: ${e.message}")
        } catch (e: Exception) {
            // Unexpected error
            _appInitState.value = AppInitializationState.Error(
                message = "Unexpected error: ${e.message}",
                errorType = InitializationErrorType.UNKNOWN,
                throwable = e
            )
            println("AppInitializer: Unexpected error during initialization")
            e.printStackTrace()
        }
    }

    fun isInitializing(): Boolean {
        return _appInitState.value.isInitializing()
    }

    /**
     * Retry initialization after an error with optional recovery strategy.
     * Allows users to retry with different recovery options.
     */
    suspend fun retryWithRecovery(recoveryOption: RecoveryOption) {
        try {
            when (recoveryOption) {
                RecoveryOption.RETRY -> {
                    // Simple retry - just run initialization again
                    initialize()
                }
                RecoveryOption.CLEAR_APP_DATA -> {
                    // Clear corrupted data and retry
                    clearAppDataAndRetry()
                }
                RecoveryOption.OFFLINE_MODE -> {
                    // Skip network check and use cached data
                    initializeOfflineMode()
                }
                RecoveryOption.RESET_PREFERENCES -> {
                    // Reset preferences to defaults and retry
                    resetPreferencesAndRetry()
                }
            }
        } catch (e: Exception) {
            // Recovery attempt failed
            _appInitState.value = AppInitializationState.Error(
                message = "Recovery failed: ${e.message}",
                errorType = InitializationErrorType.UNKNOWN,
                throwable = e
            )
            println("AppInitializer: Recovery failed - ${e.message}")
        }
    }

    /**
     * Clear all app data (database, preferences, cache) and retry initialization.
     * Used when data is corrupted or database is inaccessible.
     */
    private suspend fun clearAppDataAndRetry() {
        try {
            println("AppInitializer: Clearing app data...")

            // Clear Database tables
            database.clearAllTablesKmp()
            println("AppInitializer: Database tables cleared")

            // Clear preferences
            preferences.edit { it.clear() }
            println("AppInitializer: Preferences cleared")

            // Reset authentication manager state
            authManager.logout()
            println("AppInitializer: Authentication state reset")

            // Retry initialization with clean data
            initialize()

            println("AppInitializer: App data cleared and initialization retried successfully")
        } catch (e: Exception) {
            throw InitializationException(
                message = "Failed to clear app data: ${e.message}",
                errorType = InitializationErrorType.DATABASE,
                cause = e
            )
        }
    }

    /**
     * Initialize in offline mode - skip network requirement and use cached data.
     * Used when network is unavailable but app can function with local data.
     */
    private suspend fun initializeOfflineMode() {
        val startTime = Clock.System.now()

        try {
            _appInitState.value = AppInitializationState.Initializing

            println("AppInitializer: Initializing offline mode...")

            // Step 1: Verify Database (required even offline)
            verifyDatabase()

            // Step 2: Verify Preferences (required even offline)
            verifyPreferences()

            // Step 3: SKIP network check - we're offline

            // Step 4: Load cached user from database (don't validate with backend)
            checkAuthenticationAndLoadUser()

            // Enforce minimum splash duration
            val elapsedTime = Clock.System.now() - startTime
            if (elapsedTime < MIN_SPLASH_DURATION_MS) {
                delay(MIN_SPLASH_DURATION_MS - elapsedTime)
            }

            _appInitState.value = AppInitializationState.Success

            println("AppInitializer: Offline mode initialized successfully")
        } catch (e: Exception) {
            if (e is InitializationException) throw e

            throw InitializationException(
                message = "Failed to initialize offline mode: ${e.message}",
                errorType = InitializationErrorType.DATABASE,
                cause = e
            )
        }
    }

    /**
     * Reset preferences to defaults and retry initialization.
     * Used when preferences/configuration is corrupted.
     */
    private suspend fun resetPreferencesAndRetry() {
        try {
            println("AppInitializer: Resetting preferences to defaults...")

            // Clear preferences
            preferences.edit { it.clear() }
            println("AppInitializer: Preferences cleared")

            // Retry initialization
            initialize()

            println("AppInitializer: Preferences reset and initialization retried")
        } catch (e: Exception) {
            throw InitializationException(
                message = "Failed to reset preferences: ${e.message}",
                errorType = InitializationErrorType.PREFERENCES,
                cause = e
            )
        }
    }

    /**
     * Verify Room Database is accessible and operational.
     * Database is already built by DatabaseFactory via Koin.
     * We just verify it's working with a test query.
     */
    private suspend fun verifyDatabase() {
        try {
            // Perform a simple query to verify database is accessible
            database.ownerDao().getOwnersCount()
            println("AppInitializer: Database verified successfully")
        } catch (e: Exception) {
            throw InitializationException(
                message = "Failed to initialize database: ${e.message}",
                errorType = InitializationErrorType.DATABASE,
                cause = e
            )
        }
    }

    /**
     * Verify Preferences/DataStore is accessible.
     * DataStore is already created by Koin via createDataStore.
     * We just verify it can be accessed.
     */
    private suspend fun verifyPreferences() {
        try {
            // Read preferences once to verify DataStore is accessible
            // Using firstOrNull() instead of count() to avoid consuming the entire flow
            preferences.data.firstOrNull()
            println("AppInitializer: Preferences verified successfully")
        } catch (e: Exception) {
            throw InitializationException(
                message = "Failed to access preferences: ${e.message}",
                errorType = InitializationErrorType.PREFERENCES,
                cause = e
            )
        }
    }

    /**
     * Check network connectivity status.
     * Uses NetworkHandler to verify if device is online and network type is allowed.
     */
    private suspend fun checkNetworkStatus() {
        try {
            // Wait for a non-initializing status with a timeout
            val status = withTimeoutOrNull(2000.milliseconds) {
                networkHandler.connectivity.firstOrNull { it.type != NetworkStatusType.INITIALIZING }
            } ?: networkHandler.status()

            when(status.type) {
                NetworkStatusType.OFFLINE -> {
                    throw InitializationException(
                        message = "No network connection available",
                        errorType = InitializationErrorType.NETWORK
                    )
                }
                NetworkStatusType.ONLINE -> {
                    println("AppInitializer: Network status check passed - Type: ${status.networkType}")
                }
                NetworkStatusType.RESTRICTED -> {
                    throw InitializationException(
                        message = "Current network type (${status.networkType}) is not allowed by application policy",
                        errorType = InitializationErrorType.NETWORK
                    )
                }
                NetworkStatusType.INITIALIZING -> {
                    // This should theoretically not happen due to withTimeoutOrNull + filter,
                    // but if it does, we treat it as a failure to determine status.
                    throw InitializationException(
                        message = "Failed to determine network status (timeout)",
                        errorType = InitializationErrorType.NETWORK
                    )
                }
            }
        } catch (e: Exception) {
            if (e is InitializationException) throw e

            throw InitializationException(
                message = "Failed to check network status: ${e.message}",
                errorType = InitializationErrorType.NETWORK,
                cause = e
            )
        }
    }

    /**
     * Check if user is authenticated by loading from database.
     * The AppAuthenticationManager will react to the current user ID in DataStore.
     * We just verify accessibility here.
     */
    private suspend fun checkAuthenticationAndLoadUser() {
        try {
            // We just verify we can read the identity. 
            // AppAuthenticationManager handles the reactive loading.
            val currentUser = authManager.getCurrentUser()
            val isAuthenticated = authManager.isAuthenticated()
            println("AppInitializer: Authentication check - User management verified")
            if (currentUser != null && isAuthenticated) {
                println("AppInitializer: Authentication check - User ${currentUser.username} is authenticated")
            }
        } catch (e: Exception) {
            // If we can't check auth, it's a database issue
            throw InitializationException(
                message = "Failed to check authentication: ${e.message}",
                errorType = InitializationErrorType.DATABASE,
                cause = e
            )
        }
    }

    companion object {
        private val MIN_SPLASH_DURATION_MS = 2500.milliseconds // 2.5 seconds
    }
}
