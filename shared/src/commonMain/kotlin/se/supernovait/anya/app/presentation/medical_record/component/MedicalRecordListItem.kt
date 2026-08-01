package se.supernovait.anya.app.presentation.medical_record.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_delete_content_description
import anya.shared.generated.resources.a11y_action_edit_content_description
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.ic_edit
import anya.shared.generated.resources.ic_keyboard_arrow_right
import anya.shared.generated.resources.screen_MedicalRecord_list_item_content_description
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.list.SwipeableListItemWithActions
import se.supernovait.anya.core.presentation.common.list.rememberSwipeableListItemState
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.text.AnyaLabel
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun MedicalRecordListItem(record: MedicalRecordState, onEvent: (MedicalRecordScreenEvent) -> Unit) {
    val swipeableListItemState = rememberSwipeableListItemState()
    val actionModifier = Modifier.fillMaxHeight().padding(horizontal = MaterialTheme.spacing.medium)

    SwipeableListItemWithActions(
        state = swipeableListItemState,
        onExpanded = { swipeableListItemState.setIsActionsRevealed(true) },
        onCollapsed = { swipeableListItemState.setIsActionsRevealed(false) },
        contentDescription = stringResource(Res.string.screen_MedicalRecord_list_item_content_description, record.title),
        actions = {
            AnyaIconButton(
                icon = Res.drawable.ic_delete,
                contentDescription = stringResource(Res.string.a11y_action_delete_content_description, record.title),
                background = MaterialTheme.colorScheme.error,
                tint = MaterialTheme.colorScheme.onError,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(MedicalRecordScreenEvent.ConfirmDeleteRecord(record))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
            AnyaIconButton(
                icon = Res.drawable.ic_edit,
                contentDescription = stringResource(Res.string.a11y_action_edit_content_description, record.title),
                background = MaterialTheme.colorScheme.secondaryContainer,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(MedicalRecordScreenEvent.ShowForm(record))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
        }
    ) {
        ListItem(
            headlineContent = { Text(text = record.title, style = MaterialTheme.typography.titleLarge) },
            supportingContent = { AnyaLabel(text = "${record.date} - ${record.type.name.lowercase()}") },
            leadingContent = {
                AnyaIcon(
                    icon = record.type.icon,
                    tint = MaterialTheme.colorScheme.onSurface,
                    size = 48.dp
                )
            },
            trailingContent = { AnyaIcon(icon = Res.drawable.ic_keyboard_arrow_right) },
            modifier = Modifier.clickable(onClick = { onEvent(MedicalRecordScreenEvent.NavigateToRecord(record.id)) })
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        MedicalRecordListItem(record = PreviewData.medicalRecord, onEvent = { })
    }
}
