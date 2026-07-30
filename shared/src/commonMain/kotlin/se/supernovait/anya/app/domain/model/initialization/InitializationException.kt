package se.supernovait.anya.app.domain.model.initialization

/**
 * Custom exception for categorized initialization errors.
 */
class InitializationException(
    message: String,
    val errorType: InitializationErrorType,
    cause: Throwable? = null
) : Exception(message, cause)
