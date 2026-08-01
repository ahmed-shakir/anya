package se.supernovait.anya.app.presentation.owner.component

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
import anya.shared.generated.resources.a11y_action_share_content_description
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.ic_edit
import anya.shared.generated.resources.ic_keyboard_arrow_right
import anya.shared.generated.resources.ic_share
import anya.shared.generated.resources.screen_Owner_list_item_content_description
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.ProfileImage
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.list.SwipeableListItemWithActions
import se.supernovait.anya.core.presentation.common.list.rememberSwipeableListItemState
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun OwnerListItem(owner: OwnerState, onEvent: (OwnerScreenEvent) -> Unit) {
    val swipeableListItemState = rememberSwipeableListItemState()
    val actionModifier = Modifier.fillMaxHeight().padding(horizontal = MaterialTheme.spacing.medium)
    val fullName = "${owner.firstname} ${owner.lastname}"

    SwipeableListItemWithActions(
        state = swipeableListItemState,
        onExpanded = { swipeableListItemState.setIsActionsRevealed(true) },
        onCollapsed = { swipeableListItemState.setIsActionsRevealed(false) },
        contentDescription = stringResource(Res.string.screen_Owner_list_item_content_description, owner.name),
        actions = {
            AnyaIconButton(
                icon = Res.drawable.ic_delete,
                contentDescription = stringResource(Res.string.a11y_action_delete_content_description, fullName),
                background = MaterialTheme.colorScheme.error,
                tint = MaterialTheme.colorScheme.onError,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(OwnerScreenEvent.ConfirmDeleteOwner(owner))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
            AnyaIconButton(
                icon = Res.drawable.ic_edit,
                contentDescription = stringResource(Res.string.a11y_action_edit_content_description, fullName),
                background = MaterialTheme.colorScheme.secondaryContainer,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(OwnerScreenEvent.ShowOwnerForm(owner))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
            AnyaIconButton(
                icon = Res.drawable.ic_share,
                contentDescription = stringResource(Res.string.a11y_action_share_content_description, fullName),
                background = MaterialTheme.colorScheme.tertiary,
                tint = MaterialTheme.colorScheme.onTertiary,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(OwnerScreenEvent.ShareOwner(owner))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
        }
    ) {
        ListItem(
            headlineContent = { Text(text = "${owner.firstname} ${owner.lastname}", style = MaterialTheme.typography.titleLarge) },
            leadingContent = {
                ProfileImage(
                    uri = owner.imageUri,
                    filename = "${owner.firstname}_${owner.lastname}_${owner.dob}",
                    size = 48.dp
                )
            },
            trailingContent = { AnyaIcon(icon = Res.drawable.ic_keyboard_arrow_right) },
            modifier = Modifier.clickable(onClick = { onEvent(OwnerScreenEvent.NavigateToOwner(owner.id)) })
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        OwnerListItem(owner = PreviewData.owner, onEvent = { })
    }
}
