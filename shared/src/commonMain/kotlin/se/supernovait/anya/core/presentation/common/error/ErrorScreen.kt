package se.supernovait.anya.core.presentation.common.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.ic_satellite_dish
import anya.shared.generated.resources.screen_Error_action_retry_label
import anya.shared.generated.resources.screen_Error_description
import anya.shared.generated.resources.screen_Error_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.container.PullRefreshContainer
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaHeading
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorScreen(
    title: String = stringResource(Res.string.screen_Error_title),
    description: String = stringResource(Res.string.screen_Error_description),
    primaryActionLabel: String = stringResource(Res.string.screen_Error_action_retry_label),
    secondaryActionLabel: String? = null,
    handler: ErrorScreenHandler
) {
    val a11yButtonText = stringResource(Res.string.a11y_button)

    PullRefreshContainer(
        refreshState = rememberPullToRefreshState(),
        onRefresh = { handler.onEvent(ErrorScreenEvent.OnRetry) },
        modifier = Modifier
    ) {
        Surface(modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.small)) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(MaterialTheme.spacing.x2Large))




                Spacer(Modifier.height(MaterialTheme.spacing.x4Large))
                AnyaIcon(icon = Res.drawable.ic_satellite_dish, size = MaterialTheme.spacing.x4Large)
                Spacer(Modifier.height(MaterialTheme.spacing.extraLarge))
                AnyaHeading(text = title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(MaterialTheme.spacing.small))
                AnyaLabel(text = description)
                Spacer(Modifier.height(MaterialTheme.spacing.x4Large))

                Row(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterHorizontally)
                ) {
                    AnyaButton(
                        label = primaryActionLabel,
                        contentDescription = "$primaryActionLabel $a11yButtonText",
                        onClick = { handler.onEvent(ErrorScreenEvent.OnRetry) },
                        modifier = Modifier.weight(1f)
                    )
                    secondaryActionLabel?.let {
                        AnyaButton(
                            label = it,
                            contentDescription = "$it $a11yButtonText",
                            onClick = { handler.onEvent(ErrorScreenEvent.OnSecondaryAction) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(MaterialTheme.spacing.x2Large))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        ErrorScreen(handler = ErrorScreenHandler(onRetry = {}))
    }
}

@PreviewLightDark
@Composable
private fun PreviewWithSecondary() {
    ScreenPreviewContainer {
        ErrorScreen(
            title = "Database Error",
            description = "Unable to access app data",
            primaryActionLabel = "Retry",
            secondaryActionLabel = "Clear Data",
            handler = ErrorScreenHandler(
                onRetry = {},
                onSecondaryAction = {}
            )
        )
    }
}
