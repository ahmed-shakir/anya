package se.supernovait.anya.app.presentation.start

import se.supernovait.anya.core.domain.entity.AnyaAction

sealed interface StartScreenAction : AnyaAction {
    data object OnCensoredTextButtonClick: StartScreenAction
}
