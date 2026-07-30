package se.supernovait.anya.core.presentation.common.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.default_dialog_confirm_action_label
import anya.shared.generated.resources.default_dialog_dismiss_action_label
import anya.shared.generated.resources.ic_info
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun NotificationDialog(
    text: String,
    contentDescription: String,
    title: String = "",
    icon: DrawableResource = Res.drawable.ic_info,
    actionLabel: StringResource = Res.string.default_dialog_confirm_action_label,
    dismissLabel: StringResource = Res.string.default_dialog_dismiss_action_label,
    onDismissRequest: (() -> Unit)? = null,
    onAction: () -> Unit
) {
    AnyaDialog(
        contentDescription = contentDescription,
        onDismissRequest = { onDismissRequest?.invoke() ?: onAction() }
    ) {
        VipsDialogContent(
            title = title,
            text = text,
            icon = icon,
            actionLabel = actionLabel,
            dismissLabel = dismissLabel,
            onDismissRequest = onDismissRequest,
            onAction = onAction
        )
    }
}

@Composable
fun VipsDialogContent(
    title: String,
    text: String,
    icon: DrawableResource,
    actionLabel: StringResource,
    dismissLabel: StringResource,
    onDismissRequest: (() -> Unit)?,
    onAction: () -> Unit
) {
    Surface(
        shape = AlertDialogDefaults.shape,
        color = AlertDialogDefaults.containerColor,
        tonalElevation = AlertDialogDefaults.TonalElevation
    ) {
        Column {
            Column(modifier = Modifier.padding(MaterialTheme.spacing.large)) {
                AnyaIcon(
                    icon = icon,
                    tint = AlertDialogDefaults.titleContentColor,
                    size = AnyaActionDefaults.extraLargeStyle.iconSize,
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                )
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = AlertDialogDefaults.titleContentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(bottom = MaterialTheme.spacing.small)
                            .fillMaxWidth()
                    )
                }
                Text(
                    text = text,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AlertDialogDefaults.textContentColor,
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.divider),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                onDismissRequest?.let {
                    AnyaDialogButton(
                        label = stringResource(dismissLabel),
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                }
                AnyaDialogButton(
                    label = stringResource(actionLabel),
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
