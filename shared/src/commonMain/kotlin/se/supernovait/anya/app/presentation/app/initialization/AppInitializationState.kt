package se.supernovait.anya.app.presentation.app.initialization

import se.supernovait.anya.app.domain.model.initialization.InitializationErrorType

sealed class AppInitializationState {
    data object Initializing : AppInitializationState()
    data object Success : AppInitializationState()
    data class Error(
        val message: String,
        val errorType: InitializationErrorType,
        val throwable: Throwable? = null
    ) : AppInitializationState()

    /**
     * Returns true if initialization is still in progress.
     */
    fun isInitializing(): Boolean = this is Initializing
}
