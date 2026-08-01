package se.supernovait.anya.app.presentation.censored_text.screen

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.supernovait.anya.app.presentation.censored_text.CensoredTextEvent
import se.supernovait.anya.app.presentation.censored_text.CensoredTextScreenState
import se.supernovait.anya.app.presentation.censored_text.CensoredTextViewModel
import se.supernovait.anya.core.presentation.common.error.ErrorScreen
import se.supernovait.anya.core.presentation.common.error.ErrorScreenHandler
import se.supernovait.anya.core.presentation.common.loading.LoadingScreen

@Composable
fun CensoredTextScreen(
    viewModel: CensoredTextViewModel,
    onEvent: (CensoredTextEvent) -> Unit
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Crossfade(targetState = uiState, label = "censored_text_crossfade") { state ->
        when(state) {
            CensoredTextScreenState.Loading -> {
                LoadingScreen()
            }
            is CensoredTextScreenState.Failure -> {
                val handler = remember(onEvent) {
                    ErrorScreenHandler(onRetry = state.onRetry)
                }
                ErrorScreen(handler = handler)
            }
            is CensoredTextScreenState.Success -> {
                CensoredTextScreenContent(uiState = state, onEvent = onEvent)
            }
        }
    }
}
