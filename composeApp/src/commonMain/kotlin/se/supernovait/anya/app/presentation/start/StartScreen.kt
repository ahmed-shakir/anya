package se.supernovait.anya.app.presentation.start

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.screen_Start_action_censor_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.start.StartScreenAction.OnCensoredTextButtonClick
import se.supernovait.anya.core.presentation.common.AnyaActionSize
import se.supernovait.anya.core.presentation.common.AnyaButton
import se.supernovait.anya.core.presentation.common.AnyaScreenContainer
import se.supernovait.anya.ui.theme.spacing

/**
 * Composable that represents the user's start screen
 * @param onAction lambda that triggers different navigation actions
 */
@Composable
fun StartScreen(onAction: (StartScreenAction) -> Unit, modifier: Modifier = Modifier) {
    AnyaScreenContainer(modifier = modifier) {
        Text("Start screen - Under construction")

        Spacer(Modifier.height(MaterialTheme.spacing.medium))

        AnyaButton(
            label = stringResource(Res.string.screen_Start_action_censor_label),
            size = AnyaActionSize.LARGE,
            onClick = { onAction(OnCensoredTextButtonClick) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
