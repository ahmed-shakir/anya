package se.supernovait.anya.app.presentation.welcome

import se.supernovait.anya.core.domain.entity.AnyaAction

sealed interface WelcomeScreenAction : AnyaAction {
    data object OnStartButtonClick: WelcomeScreenAction
    data object OnInfoButtonClick: WelcomeScreenAction
}
