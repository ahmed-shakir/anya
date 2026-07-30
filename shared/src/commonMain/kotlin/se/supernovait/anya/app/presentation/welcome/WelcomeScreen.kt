package se.supernovait.anya.app.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.app_icon
import anya.shared.generated.resources.app_logo_content_description
import anya.shared.generated.resources.created_by
import anya.shared.generated.resources.ic_info
import anya.shared.generated.resources.ic_logout
import anya.shared.generated.resources.screen_Welcome_action_info_content_description
import anya.shared.generated.resources.screen_Welcome_action_sign_in_label
import anya.shared.generated.resources.screen_Welcome_action_sign_up_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.welcome.component.SignInForm
import se.supernovait.anya.app.presentation.welcome.component.SignupForm
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.action.AnyaTextAction
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaCopyright
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

/**
 * Composable that welcomes the user to Anya app
 * @param onEvent lambda that triggers different navigation actions
 */
@Composable
fun WelcomeScreen(
    uiState: WelcomeScreenState,
    onEvent: (WelcomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val signUpButtonLabel = stringResource(Res.string.screen_Welcome_action_sign_up_label)
    val signInButtonLabel = stringResource(Res.string.screen_Welcome_action_sign_in_label)

    if(uiState.showSignUpForm) {
        SignupForm(onEvent = onEvent)
    }

    if(uiState.showSignInForm) {
        SignInForm(uiState = uiState, onEvent = onEvent)
    }

    ScreenContainer(modifier = modifier) {
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = stringResource(Res.string.app_logo_content_description),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .align(Alignment.CenterHorizontally)
                .padding(top = MaterialTheme.spacing.x3Large)
        )

        Spacer(Modifier.height(MaterialTheme.spacing.x3Large))

        AnyaButton(
            label = signUpButtonLabel,
            contentDescription = "$signUpButtonLabel $a11yButtonText",
            textStyle = MaterialTheme.typography.headlineLarge,
            onClick = { onEvent(WelcomeScreenEvent.ShowSignUpForm) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(MaterialTheme.spacing.medium))

        AnyaTextAction(
            label = signInButtonLabel,
            contentDescription = "$signInButtonLabel $a11yButtonText",
            icon = Res.drawable.ic_logout,
            textStyle = MaterialTheme.typography.headlineLarge,
            onClick = { onEvent(WelcomeScreenEvent.ShowSignInForm) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnyaCopyright(text = stringResource(Res.string.created_by))
            AnyaIconButton(
                icon = Res.drawable.ic_info,
                contentDescription = stringResource(Res.string.screen_Welcome_action_info_content_description),
                iconSize = AnyaActionDefaults.smallStyle.iconSize,
                onClick = { onEvent(WelcomeScreenEvent.NavigateToInfo) }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        WelcomeScreen(uiState = WelcomeScreenState(), onEvent = { })
    }
}
