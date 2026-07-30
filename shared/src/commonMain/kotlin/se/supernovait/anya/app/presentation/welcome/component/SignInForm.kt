package se.supernovait.anya.app.presentation.welcome.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.screen_Welcome_sign_in_form_action_sign_in_label
import anya.shared.generated.resources.screen_Welcome_sign_in_form_content_description
import anya.shared.generated.resources.screen_Welcome_sign_in_form_title
import anya.shared.generated.resources.screen_Welcome_sign_in_form_username_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenEvent
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenState
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.modal.AnyaBottomSheet
import se.supernovait.anya.core.presentation.common.text.AnyaTitle

@Composable
fun SignInForm(
    uiState: WelcomeScreenState,
    onEvent: (WelcomeScreenEvent) -> Unit
) {
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val signInButtonLabel = stringResource(Res.string.screen_Welcome_sign_in_form_action_sign_in_label)

    AnyaBottomSheet(
        contentDescription = stringResource(Res.string.screen_Welcome_sign_in_form_content_description),
        onDismissRequest = { onEvent(WelcomeScreenEvent.HideSignInForm) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            AnyaTitle(
                text = stringResource(Res.string.screen_Welcome_sign_in_form_title),
                modifier = Modifier.padding(start = MaterialTheme.spacing.small)
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_Welcome_sign_in_form_username_label),
                initialValue = uiState.username,
                onValueChange = { value, _ -> onEvent(WelcomeScreenEvent.OnUsernameChange(value)) }
            )

            AnyaButton(
                label = signInButtonLabel,
                contentDescription = "$signInButtonLabel $a11yButtonText",
                onClick = { onEvent(WelcomeScreenEvent.SignIn(uiState.username)) },
                enabled = uiState.username.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
