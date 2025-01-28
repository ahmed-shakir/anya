package se.supernovait.anya.app.presentation.censored_text

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.screen_CensoredText_action_censor_label
import anya.composeapp.generated.resources.screen_CensoredText_counter_text
import anya.composeapp.generated.resources.screen_CensoredText_textField_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import se.supernovait.anya.app.presentation.censored_text.CensoredTextAction.OnCensorButtonClick
import se.supernovait.anya.app.presentation.censored_text.CensoredTextAction.OnUncensoredTextUpdate
import se.supernovait.anya.core.domain.entity.validation.ValidationRules
import se.supernovait.anya.core.presentation.common.AnyaButton
import se.supernovait.anya.core.presentation.common.AnyaScreenContainer
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.loading.AnyaLoadingIndicator
import se.supernovait.anya.core.presentation.common.text.AnyaLabel
import se.supernovait.anya.ui.theme.spacing

/**
 * Composable that lets the user to censor bad words
 * @param state the screen UI state
 * @param onAction lambda that triggers different actions
 */
@Preview
@Composable
fun CensoredTextScreen(
    state: CensoredTextState,
    onAction: (CensoredTextAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if(state.isLoading) {
        AnyaLoadingIndicator(isLoading = state.isLoading)
    } else {
        AnyaScreenContainer(modifier = modifier) {
            AnyaTextField(
                label = stringResource(Res.string.screen_CensoredText_textField_label),
                validationRules = ValidationRules.defaultRules,
                onValueChange = {value, _ -> onAction(OnUncensoredTextUpdate(value)) }
            )
            AnyaButton(
                label = stringResource(Res.string.screen_CensoredText_action_censor_label),
                loading = state.isLoading,
                onClick = { onAction(OnCensorButtonClick) },
                modifier = Modifier.fillMaxWidth()
            )
            state.censoredText?.let { AnyaLabel(text = it) }

            Spacer(Modifier.height(MaterialTheme.spacing.medium))

            AnyaLabel(
                text = stringResource(Res.string.screen_CensoredText_counter_text, state.counter),
                textAlign = TextAlign.Center
            )
        }
    }
}
