package se.supernovait.anya.app.presentation.censored_text.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.screen_CensoredText_action_censor_label
import anya.shared.generated.resources.screen_CensoredText_counter_text
import anya.shared.generated.resources.screen_CensoredText_section_results_title
import anya.shared.generated.resources.screen_CensoredText_textField_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.censored_text.CensoredTextEvent
import se.supernovait.anya.app.presentation.censored_text.CensoredTextScreenState
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.container.ScreenSection
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

/**
 * Composable that lets the user censor bad words
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun CensoredTextScreenContent(
    uiState: CensoredTextScreenState.Success,
    onEvent: (CensoredTextEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenContainer(modifier = modifier) {
        AnyaTextField(
            label = stringResource(Res.string.screen_CensoredText_textField_label),
            onValueChange = {value, _ -> onEvent(CensoredTextEvent.UpdateUncensoredText(text = value)) }
        )
        AnyaButton(
            label = stringResource(Res.string.screen_CensoredText_action_censor_label),
            onClick = { onEvent(CensoredTextEvent.CensorText) },
            modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.medium)
        )
        AnyaLabel(
            text = stringResource(Res.string.screen_CensoredText_counter_text, uiState.counter),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.large)
        )

        uiState.censoredText?.let {
            ScreenSection(title = stringResource(Res.string.screen_CensoredText_section_results_title)) {
                AnyaLabel(text = it)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        CensoredTextScreenContent(uiState = CensoredTextScreenState.Success(), onEvent = {})
    }
}
