package se.supernovait.anya.core.presentation.common.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.ic_circle_exclamation
import anya.shared.generated.resources.network_status_offline_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.sizing
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.app.theme.statusColor
import se.supernovait.anya.core.domain.network.NetworkStatusType
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

@Composable
fun NetworkIndicator(
    type: NetworkStatusType?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = type == NetworkStatusType.OFFLINE,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.statusColor.error)
                .padding(MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnyaIcon(
                icon = Res.drawable.ic_circle_exclamation,
                tint = MaterialTheme.colorScheme.onError,
                size = MaterialTheme.sizing.icon.small
            )
            AnyaLabel(
                text = stringResource(Res.string.network_status_offline_label),
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        NetworkIndicator(type = NetworkStatusType.OFFLINE)
    }
}
