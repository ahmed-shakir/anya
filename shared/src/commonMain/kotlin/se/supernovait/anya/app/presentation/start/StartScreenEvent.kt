package se.supernovait.anya.app.presentation.start

import se.supernovait.anya.core.domain.model.AnyaEvent

sealed interface StartScreenEvent : AnyaEvent {
    data object NavigateToCatScreen: StartScreenEvent
    data object NavigateToCensoredTextScreen: StartScreenEvent
    data object NavigateToInfoScreen: StartScreenEvent
    data object NavigateToOwnerScreen: StartScreenEvent
    data object NavigateToProfileScreen: StartScreenEvent
    data object SignOut: StartScreenEvent
}
