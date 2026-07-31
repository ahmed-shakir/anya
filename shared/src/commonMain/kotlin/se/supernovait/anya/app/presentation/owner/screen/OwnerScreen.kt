package se.supernovait.anya.app.presentation.owner.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_dialog_delete_confirmation_content_description
import anya.shared.generated.resources.dialog_delete_confirmation_message
import anya.shared.generated.resources.dialog_delete_confirmation_title
import anya.shared.generated.resources.ic_add
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.screen_Owner_fab_action_add_content_description
import anya.shared.generated.resources.screen_Owner_search_label
import anya.shared.generated.resources.user_profile_action_content_description
import anya.shared.generated.resources.user_profile_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.domain.model.sort.OwnerSortOption
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.auth.userId
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarAction
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.component.OwnerForm
import se.supernovait.anya.app.presentation.owner.component.OwnerListItem
import se.supernovait.anya.app.presentation.owner.state.OwnerScreenState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.input_field.AnyaSearchField
import se.supernovait.anya.core.presentation.common.menu.sort.SortDropdownMenu
import se.supernovait.anya.core.presentation.common.modal.NotificationDialog
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer

/**
 * Composable that lets the users see cat owners
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun OwnerScreen(
    uiState: OwnerScreenState,
    onEvent: (OwnerScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val authState = LocalAuthState.current
    val topBarState = LocalTopBarState.current
    val fabState = LocalFabState.current

    val profileLabel = stringResource(Res.string.user_profile_label)
    val profileContentDescription = stringResource(Res.string.user_profile_action_content_description)

    if(uiState.showOwnerForm) {
        OwnerForm(owner = uiState.selectedOwner ?: OwnerState.empty, onEvent = onEvent)
    }

    uiState.ownerToDelete?.let { owner ->
        NotificationDialog(
            title = stringResource(Res.string.dialog_delete_confirmation_title),
            text = stringResource(Res.string.dialog_delete_confirmation_message, owner.name),
            contentDescription = stringResource(Res.string.a11y_dialog_delete_confirmation_content_description, owner.name),
            icon = Res.drawable.ic_delete,
            onDismissRequest = { onEvent(OwnerScreenEvent.DismissDeleteConfirmation) },
            onAction = { onEvent(OwnerScreenEvent.DeleteOwner(owner)) }
        )
    }

    DisposableEffect(Unit) {
        topBarState.actions(
            actions = listOf(TopBarAction(
                icon = Res.drawable.ic_person,
                label = profileLabel,
                contentDescription = profileContentDescription,
                onClick = { onEvent(OwnerScreenEvent.NavigateToOwner(id = authState.userId)) }
            ))
        )
        onDispose { topBarState.clearActions() }
    }

    DisposableEffect(Unit) {
        fabState.set(
            icon = Res.drawable.ic_add,
            contentDescription = Res.string.screen_Owner_fab_action_add_content_description,
            onClick = { onEvent(OwnerScreenEvent.ShowOwnerForm()) }
        )
        onDispose { fabState.clear() }
    }

    ScreenContainer(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.spacing.large)
        ) {
            AnyaSearchField(
                placeholder = stringResource(Res.string.screen_Owner_search_label),
                onSearch = { query -> onEvent(OwnerScreenEvent.FilterOwners(query)) },
                fullWidth = false,
                modifier = Modifier.fillMaxWidth(fraction = 0.85f)
            )
            SortDropdownMenu(
                allOptions = OwnerSortOption.entries,
                selectedSortOption = uiState.selectedSortOption,
                onSortOptionSelected = { sortOption -> onEvent(OwnerScreenEvent.SortOwners(sortOption)) }
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = uiState.owners, key = { it.id }) { owner ->
                OwnerListItem(owner = owner, onEvent = onEvent)
                HorizontalDivider()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        OwnerScreen(uiState = OwnerScreenState(owners = listOf(PreviewData.owner)), onEvent = {})
    }
}
