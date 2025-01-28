package se.supernovait.anya.app.presentation.censored_text

import se.supernovait.anya.core.domain.entity.AnyaAction

interface CensoredTextAction : AnyaAction {
    data class OnUncensoredTextUpdate(val text: String): CensoredTextAction
    data object OnCensorButtonClick: CensoredTextAction
}
