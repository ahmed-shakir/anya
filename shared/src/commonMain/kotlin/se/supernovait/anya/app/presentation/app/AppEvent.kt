package se.supernovait.anya.app.presentation.app

import se.supernovait.anya.core.domain.model.error.NetworkError

sealed interface AppEvent {
    data class Error(val error: NetworkError): AppEvent
    data class Message(val message: String): AppEvent
    data object NavigateBack: AppEvent
    data object SignIn: AppEvent
}
