package se.supernovait.anya.app.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.created_by
import anya.composeapp.generated.resources.info_action_icon_description
import anya.composeapp.generated.resources.logo_description
import anya.composeapp.generated.resources.screen_Welcome_action_start_label
import anya.composeapp.generated.resources.supernova_logo_star_color_v2
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenAction.OnInfoButtonClick
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenAction.OnStartButtonClick
import se.supernovait.anya.core.presentation.common.AnyaActionSize
import se.supernovait.anya.core.presentation.common.AnyaButton
import se.supernovait.anya.core.presentation.common.AnyaIconButton
import se.supernovait.anya.core.presentation.common.AnyaScreenContainer
import se.supernovait.anya.core.presentation.common.text.AnyaCopyright
import se.supernovait.anya.ui.theme.spacing

/**
 * Composable that welcomes the user to Anya app
 * @param onAction lambda that triggers different navigation actions
 */
@Composable
fun WelcomeScreen(onAction: (WelcomeScreenAction) -> Unit, modifier: Modifier = Modifier) {
    AnyaScreenContainer(modifier = modifier) {
        Image(
            painter = painterResource(Res.drawable.supernova_logo_star_color_v2),
            contentDescription = stringResource(Res.string.logo_description),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .align(Alignment.CenterHorizontally)
                .padding(top = MaterialTheme.spacing.extraLarge)
        )

        Spacer(Modifier.height(MaterialTheme.spacing.extraLarge))

        AnyaButton(
            label = stringResource(Res.string.screen_Welcome_action_start_label),
            size = AnyaActionSize.LARGE,
            onClick = { onAction(OnStartButtonClick) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnyaCopyright(text = stringResource(Res.string.created_by))
            AnyaIconButton(
                icon = Icons.Outlined.Info,
                descriptionId = Res.string.info_action_icon_description,
                size = AnyaActionSize.SMALL,
                onClick = { onAction(OnInfoButtonClick) }
            )
        }
    }
}
