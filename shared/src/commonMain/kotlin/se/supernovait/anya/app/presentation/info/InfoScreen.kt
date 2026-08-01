package se.supernovait.anya.app.presentation.info

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
import anya.shared.generated.resources.app_family
import anya.shared.generated.resources.app_icon
import anya.shared.generated.resources.app_logo_content_description
import anya.shared.generated.resources.app_version
import anya.shared.generated.resources.created_by
import anya.shared.generated.resources.current_battery_level
import anya.shared.generated.resources.device
import anya.shared.generated.resources.network_status
import anya.shared.generated.resources.screen_Info_text_quote
import anya.shared.generated.resources.screen_Info_title
import anya.shared.generated.resources.supernova_logo_star_color_v2
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.sizing
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaCopyright
import se.supernovait.anya.core.presentation.common.text.AnyaLabel
import se.supernovait.anya.core.presentation.common.text.AnyaQuote
import se.supernovait.anya.core.presentation.common.text.AnyaTitle

/**
 * Composable that shows information about Anya app
 */
@Composable
fun InfoScreen(uiState: InfoScreenState, modifier: Modifier = Modifier) {
    ScreenContainer(modifier = modifier) {
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = stringResource(Res.string.app_logo_content_description),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .align(Alignment.CenterHorizontally)
                .padding(top = MaterialTheme.spacing.large)
        )
        AnyaTitle(text = stringResource(Res.string.screen_Info_title))
        AnyaQuote(text = stringResource(Res.string.screen_Info_text_quote))

        Spacer(Modifier.height(MaterialTheme.spacing.medium))

        InfoCard {
            AnyaCopyright(text = stringResource(Res.string.created_by))
            AnyaCopyright(text = stringResource(Res.string.app_version, "0.9.0"))
            AnyaCopyright(text = stringResource(Res.string.device, uiState.platform.name))
            AnyaCopyright(text = stringResource(Res.string.network_status, uiState.networkStatus))
            AnyaCopyright(text = stringResource(Res.string.current_battery_level, uiState.batteryLevel))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.large)
        ) {
            AnyaIcon(
                icon = Res.drawable.supernova_logo_star_color_v2,
                size = MaterialTheme.sizing.icon.default
            )
            AnyaLabel(text = stringResource(Res.string.app_family))
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        InfoScreen(uiState = InfoScreenState())
    }
}
