package se.supernovait.anya.app.presentation.censored_text

import se.supernovait.anya.core.domain.model.AnyaEvent

sealed interface CensoredTextEvent : AnyaEvent {
    data class UpdateUncensoredText(val text: String): CensoredTextEvent
    data object CensorText: CensoredTextEvent
}
