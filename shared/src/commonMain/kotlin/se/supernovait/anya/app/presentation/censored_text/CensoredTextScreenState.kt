package se.supernovait.anya.app.presentation.censored_text

import androidx.compose.runtime.Stable

sealed interface CensoredTextScreenState {
    data object Loading: CensoredTextScreenState

    data class Failure(val onRetry: () -> Unit): CensoredTextScreenState

    @Stable
    data class Success(
        val censoredText: String? = null,
        val counter: Int = 0
    ): CensoredTextScreenState
}
