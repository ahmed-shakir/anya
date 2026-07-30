package se.supernovait.anya.core.presentation.common.error

sealed interface ErrorScreenEvent {
    data object OnRetry: ErrorScreenEvent
    data object OnSecondaryAction: ErrorScreenEvent
}
