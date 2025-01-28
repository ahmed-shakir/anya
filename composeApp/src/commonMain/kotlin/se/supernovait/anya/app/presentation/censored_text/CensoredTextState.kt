package se.supernovait.anya.app.presentation.censored_text

import androidx.compose.runtime.Stable

@Stable
data class CensoredTextState(
    val censoredText: String? = null,
    val uncensoredText: String = "",
    val isLoading: Boolean = false,
    val counter: Int = 0
)
