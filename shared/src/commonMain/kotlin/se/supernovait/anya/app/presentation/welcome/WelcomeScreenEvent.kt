package se.supernovait.anya.app.presentation.welcome

import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.model.AnyaEvent

sealed interface WelcomeScreenEvent : AnyaEvent {
    data object NavigateToInfo: WelcomeScreenEvent
    data class SignUp(val profile: OwnerState): WelcomeScreenEvent
    data class SignIn(val username: String): WelcomeScreenEvent
    data class OnUsernameChange(val username: String): WelcomeScreenEvent
    data object ShowSignUpForm: WelcomeScreenEvent
    data object HideSignUpForm: WelcomeScreenEvent
    data object ShowSignInForm: WelcomeScreenEvent
    data object HideSignInForm: WelcomeScreenEvent
}
