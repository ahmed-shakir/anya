package se.supernovait.anya.app.presentation.medical_record.screen

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
import anya.shared.generated.resources.screen_MedicalRecord_fab_action_add_content_description
import anya.shared.generated.resources.screen_MedicalRecord_search_label
import anya.shared.generated.resources.user_profile_action_content_description
import anya.shared.generated.resources.user_profile_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.domain.model.sort.MedicalRecordSortOption
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.auth.userId
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarAction
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.component.MedicalRecordForm
import se.supernovait.anya.app.presentation.medical_record.component.MedicalRecordListItem
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordScreenState
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.input_field.AnyaSearchField
import se.supernovait.anya.core.presentation.common.menu.sort.SortDropdownMenu
import se.supernovait.anya.core.presentation.common.modal.NotificationDialog
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer

/**
 * Composable that lets the users see their cat's medical records
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun MedicalRecordScreen(
    uiState: MedicalRecordScreenState,
    onEvent: (MedicalRecordScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val authState = LocalAuthState.current
    val topBarState = LocalTopBarState.current
    val fabState = LocalFabState.current

    val profileLabel = stringResource(Res.string.user_profile_label)
    val profileContentDescription = stringResource(Res.string.user_profile_action_content_description)

    if(uiState.showForm) {
        MedicalRecordForm(record = uiState.selectedRecord ?: MedicalRecordState.empty, onEvent = onEvent)
    }

    uiState.recordToDelete?.let { record ->
        NotificationDialog(
            title = stringResource(Res.string.dialog_delete_confirmation_title),
            text = stringResource(Res.string.dialog_delete_confirmation_message, record.title),
            contentDescription = stringResource(Res.string.a11y_dialog_delete_confirmation_content_description, record.title),
            icon = Res.drawable.ic_delete,
            onDismissRequest = { onEvent(MedicalRecordScreenEvent.DismissDeleteConfirmation) },
            onAction = { onEvent(MedicalRecordScreenEvent.DeleteRecord(record)) }
        )
    }

    DisposableEffect(Unit) {
        topBarState.actions(
            actions = listOf(TopBarAction(
                icon = Res.drawable.ic_person,
                label = profileLabel,
                contentDescription = profileContentDescription,
                onClick = { onEvent(MedicalRecordScreenEvent.NavigateToOwner(id = authState.userId)) }
            ))
        )
        onDispose { topBarState.clearActions() }
    }

    DisposableEffect(Unit) {
        fabState.set(
            icon = Res.drawable.ic_add,
            contentDescription = Res.string.screen_MedicalRecord_fab_action_add_content_description,
            onClick = { onEvent(MedicalRecordScreenEvent.ShowForm()) }
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
                placeholder = stringResource(Res.string.screen_MedicalRecord_search_label),
                onSearch = { query -> onEvent(MedicalRecordScreenEvent.FilterRecords(query)) },
                fullWidth = false,
                modifier = Modifier.fillMaxWidth(fraction = 0.9f)
            )
            SortDropdownMenu(
                allOptions = MedicalRecordSortOption.entries,
                selectedSortOption = uiState.selectedSortOption,
                onSortOptionSelected = { sortOption -> onEvent(MedicalRecordScreenEvent.SortRecords(sortOption)) }
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = uiState.records, key = { it.id }) { record ->
                MedicalRecordListItem(record = record, onEvent = onEvent)
                HorizontalDivider()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        MedicalRecordScreen(uiState = MedicalRecordScreenState(records = listOf(PreviewData.medicalRecord)), onEvent = {})
    }
}
