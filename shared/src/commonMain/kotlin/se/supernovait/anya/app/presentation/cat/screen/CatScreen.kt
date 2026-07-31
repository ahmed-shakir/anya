package se.supernovait.anya.app.presentation.cat.screen

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
import anya.shared.generated.resources.screen_Cat_fab_action_add_content_description
import anya.shared.generated.resources.screen_Cat_search_label
import anya.shared.generated.resources.user_profile_action_content_description
import anya.shared.generated.resources.user_profile_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.domain.model.sort.CatSortOption
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.auth.userId
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarAction
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.component.CatForm
import se.supernovait.anya.app.presentation.cat.component.CatListItem
import se.supernovait.anya.app.presentation.cat.state.CatScreenState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.input_field.AnyaSearchField
import se.supernovait.anya.core.presentation.common.menu.sort.SortDropdownMenu
import se.supernovait.anya.core.presentation.common.modal.NotificationDialog
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer

/**
 * Composable that lets the users see their cats
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun CatScreen(
    uiState: CatScreenState,
    onEvent: (CatScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val authState = LocalAuthState.current
    val topBarState = LocalTopBarState.current
    val fabState = LocalFabState.current

    val profileLabel = stringResource(Res.string.user_profile_label)
    val profileContentDescription = stringResource(Res.string.user_profile_action_content_description)

    if(uiState.showCatForm) {
        CatForm(cat = uiState.selectedCat ?: CatState.empty, onEvent = onEvent)
    }

    uiState.catToDelete?.let { cat ->
        NotificationDialog(
            title = stringResource(Res.string.dialog_delete_confirmation_title),
            text = stringResource(Res.string.dialog_delete_confirmation_message, cat.name),
            contentDescription = stringResource(Res.string.a11y_dialog_delete_confirmation_content_description, cat.name),
            icon = Res.drawable.ic_delete,
            onDismissRequest = { onEvent(CatScreenEvent.DismissDeleteConfirmation) },
            onAction = { onEvent(CatScreenEvent.DeleteCat(cat)) }
        )
    }

    DisposableEffect(Unit) {
        topBarState.actions(
            actions = listOf(TopBarAction(
                icon = Res.drawable.ic_person,
                label = profileLabel,
                contentDescription = profileContentDescription,
                onClick = { onEvent(CatScreenEvent.NavigateToOwner(id = authState.userId)) }
            ))
        )
        onDispose { topBarState.clearActions() }
    }

    DisposableEffect(Unit) {
        fabState.set(
            icon = Res.drawable.ic_add,
            contentDescription = Res.string.screen_Cat_fab_action_add_content_description,
            onClick = { onEvent(CatScreenEvent.ShowCatForm()) }
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
                placeholder = stringResource(Res.string.screen_Cat_search_label),
                onSearch = { query -> onEvent(CatScreenEvent.FilterCats(query)) },
                fullWidth = false,
                modifier = Modifier.fillMaxWidth(fraction = 0.85f)
            )
            SortDropdownMenu(
                allOptions = CatSortOption.entries,
                selectedSortOption = uiState.selectedSortOption,
                onSortOptionSelected = { sortOption -> onEvent(CatScreenEvent.SortCats(sortOption)) }
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = uiState.cats, key = { it.id }) { cat ->
                CatListItem(cat = cat, onEvent = onEvent)
                HorizontalDivider()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        CatScreen(uiState = CatScreenState(cats = listOf(PreviewData.cat)), onEvent = {})
    }
}
