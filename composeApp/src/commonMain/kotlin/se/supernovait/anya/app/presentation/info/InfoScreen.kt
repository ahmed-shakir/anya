package se.supernovait.anya.app.presentation.info

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.app_version
import anya.composeapp.generated.resources.created_by
import anya.composeapp.generated.resources.device
import anya.composeapp.generated.resources.screen_Info_text_quote
import anya.composeapp.generated.resources.screen_Info_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.core.presentation.common.AnyaScreenContainer
import se.supernovait.anya.core.presentation.common.text.AnyaCopyright
import se.supernovait.anya.core.presentation.common.text.AnyaQuote
import se.supernovait.anya.core.presentation.common.text.AnyaTitle
import se.supernovait.anya.ui.theme.spacing

/**
 * Composable that shows information about Anya app
 */
@Composable
fun InfoScreen(uiState: InfoScreenState, modifier: Modifier = Modifier) {
    AnyaScreenContainer(modifier = modifier) {
        AnyaTitle(text = stringResource(Res.string.screen_Info_title))
        AnyaQuote(
            text = stringResource(Res.string.screen_Info_text_quote),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )

        Spacer(Modifier.height(MaterialTheme.spacing.medium))

        InfoCard {
            AnyaCopyright(text = stringResource(Res.string.created_by))
            AnyaCopyright(text = stringResource(Res.string.app_version, "0.9.0"))
            AnyaCopyright(text = stringResource(Res.string.device, uiState.platform.name))
            AnyaCopyright(text = "Current battery level: ${uiState.batteryLevel}")
        }
    }
}
