package se.supernovait.anya.app.presentation

import se.supernovait.anya.core.domain.entity.NetworkError

sealed interface AnyaEvent {
    data class Error(val error: NetworkError): AnyaEvent
}
