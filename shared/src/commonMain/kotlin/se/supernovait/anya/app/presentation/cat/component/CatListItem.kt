package se.supernovait.anya.app.presentation.cat.component

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
import anya.shared.generated.resources.datetime_months_old
import anya.shared.generated.resources.datetime_years_old
import anya.shared.generated.resources.ic_cat
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.ic_edit
import anya.shared.generated.resources.ic_keyboard_arrow_right
import anya.shared.generated.resources.ic_share
import anya.shared.generated.resources.screen_Cat_image_description
import anya.shared.generated.resources.screen_Cat_list_item_content_description
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.domain.util.monthsUntilNow
import se.supernovait.anya.core.domain.util.yearsUntilNow
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.ProfileImage
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.list.SwipeableListItemWithActions
import se.supernovait.anya.core.presentation.common.list.rememberSwipeableListItemState
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.text.AnyaLabel
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun CatListItem(cat: CatState, onEvent: (CatScreenEvent) -> Unit) {
    val swipeableListItemState = rememberSwipeableListItemState()
    val actionModifier = Modifier.fillMaxHeight().padding(horizontal = MaterialTheme.spacing.medium)

    SwipeableListItemWithActions(
        state = swipeableListItemState,
        onExpanded = { swipeableListItemState.setIsActionsRevealed(true) },
        onCollapsed = { swipeableListItemState.setIsActionsRevealed(false) },
        contentDescription = stringResource(Res.string.screen_Cat_list_item_content_description, cat.name),
        actions = {
            AnyaIconButton(
                icon = Res.drawable.ic_delete,
                contentDescription = stringResource(Res.string.a11y_action_delete_content_description, cat.name),
                background = MaterialTheme.colorScheme.error,
                tint = MaterialTheme.colorScheme.onError,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(CatScreenEvent.ConfirmDeleteCat(cat))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
            AnyaIconButton(
                icon = Res.drawable.ic_edit,
                contentDescription = stringResource(Res.string.a11y_action_edit_content_description, cat.name),
                background = MaterialTheme.colorScheme.secondaryContainer,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(CatScreenEvent.ShowCatForm(cat))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
            AnyaIconButton(
                icon = Res.drawable.ic_share,
                contentDescription = stringResource(Res.string.a11y_action_share_content_description, cat.name),
                background = MaterialTheme.colorScheme.tertiary,
                tint = MaterialTheme.colorScheme.onTertiary,
                iconSize = AnyaActionDefaults.largeStyle.iconSize,
                modifier = actionModifier,
                onClick = {
                    onEvent(CatScreenEvent.ShareCat(cat))
                    swipeableListItemState.setIsActionsRevealed(false)
                }
            )
        }
    ) {
        ListItem(
            headlineContent = { Text(text = cat.nickname, style = MaterialTheme.typography.titleLarge) },
            supportingContent = { AnyaLabel(text = "${cat.breed}, ${calculateAge(cat.dob)}") },
            leadingContent = {
                ProfileImage(
                    uri = cat.imageUri,
                    filename = "${cat.name}_${cat.dob}",
                    placeholder = Res.drawable.ic_cat,
                    description = stringResource(Res.string.screen_Cat_image_description),
                    size = 48.dp
                )
            },
            trailingContent = { AnyaIcon(icon = Res.drawable.ic_keyboard_arrow_right) },
            modifier = Modifier.clickable(onClick = { onEvent(CatScreenEvent.NavigateToCat(cat.id)) })
        )
    }
}

@Composable
private fun calculateAge(dob: LocalDate?): String {
    val years = dob.yearsUntilNow()
    val months = dob.monthsUntilNow()
    return if (years > 0) stringResource(Res.string.datetime_years_old, years)
    else stringResource(Res.string.datetime_months_old, months)
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        CatListItem(cat = PreviewData.cat, onEvent = { })
    }
}
