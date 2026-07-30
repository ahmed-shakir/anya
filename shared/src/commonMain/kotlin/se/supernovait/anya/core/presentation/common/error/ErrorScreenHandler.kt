package se.supernovait.anya.core.presentation.common.error

import se.supernovait.anya.core.domain.handler.EventHandler

/**
 * ErrorScreenHandler that handles primary retry and optional secondary recovery action.
 *
 * Usage:
 * val handler = ErrorScreenHandler(
 *     onRetry = { appInitializer.retryWithRecovery(RecoveryOption.Retry) },
 *     onSecondaryAction = { appInitializer.retryWithRecovery(RecoveryOption.ClearAppData) }
 * )
 *
 * ErrorScreen(
 *     title = "Database Error",
 *     description = "Unable to access app data",
 *     buttonText = "Retry",
 *     secondaryButtonText = "Clear Data",  // Optional
 *     handler = handler
 * )
 */
data class ErrorScreenHandler(
    val onRetry: () -> Unit,
    val onSecondaryAction: (() -> Unit)? = null
) : EventHandler<ErrorScreenEvent> {
    override fun onEvent(event: ErrorScreenEvent) {
        when(event) {
            ErrorScreenEvent.OnRetry -> {
                onRetry.invoke()
            }
            ErrorScreenEvent.OnSecondaryAction -> {
                onSecondaryAction?.invoke()
            }
        }
    }
}
