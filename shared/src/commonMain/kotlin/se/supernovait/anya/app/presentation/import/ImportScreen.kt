package se.supernovait.anya.app.presentation.import

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.default_dialog_cancel_action_label
import anya.shared.generated.resources.screen_Import_action_import_label
import anya.shared.generated.resources.screen_Import_action_view_details_label
import anya.shared.generated.resources.screen_Import_message_cat
import anya.shared.generated.resources.screen_Import_message_owner
import anya.shared.generated.resources.screen_Import_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.domain.model.ShareType
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.action.AnyaOutlinedButton
import se.supernovait.anya.core.presentation.common.action.AnyaTextAction
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

@Composable
fun ImportScreen(
    uiState: ImportScreenState,
    onEvent: (ImportScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val importButtonLabel = stringResource(Res.string.screen_Import_action_import_label)
    val viewDetailsButtonLabel = stringResource(Res.string.screen_Import_action_view_details_label)
    val cancelButtonLabel = stringResource(Res.string.default_dialog_cancel_action_label)

    ScreenContainer(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnyaLabel(
                text = stringResource(Res.string.screen_Import_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            val message = when (uiState.type) {
                ShareType.CAT -> stringResource(Res.string.screen_Import_message_cat, uiState.name)
                ShareType.OWNER -> stringResource(Res.string.screen_Import_message_owner, uiState.name)
                else -> ""
            }

            AnyaLabel(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.x2Large))

            AnyaButton(
                label = importButtonLabel,
                contentDescription = "$importButtonLabel $a11yButtonText",
                onClick = { onEvent(ImportScreenEvent.Import) },
                loading = uiState.isImporting,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            AnyaOutlinedButton(
                label = viewDetailsButtonLabel,
                contentDescription = "$viewDetailsButtonLabel $a11yButtonText",
                onClick = { onEvent(ImportScreenEvent.ViewDetails) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AnyaTextAction(
                label = cancelButtonLabel,
                contentDescription = "$cancelButtonLabel $a11yButtonText",
                onClick = { onEvent(ImportScreenEvent.Cancel) }
            )
        }
    }
}
