package se.supernovait.anya.app.presentation.medical_record.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_delete_content_description
import anya.shared.generated.resources.a11y_dialog_delete_confirmation_content_description
import anya.shared.generated.resources.delete_action_label
import anya.shared.generated.resources.dialog_delete_confirmation_message
import anya.shared.generated.resources.dialog_delete_confirmation_title
import anya.shared.generated.resources.ic_check
import anya.shared.generated.resources.ic_close
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.screen_MedicalRecord_form_contagious_label
import anya.shared.generated.resources.screen_MedicalRecord_form_date_label
import anya.shared.generated.resources.screen_MedicalRecord_form_description_label
import anya.shared.generated.resources.screen_MedicalRecord_form_title_label
import anya.shared.generated.resources.screen_MedicalRecord_form_type_label
import anya.shared.generated.resources.screen_MedicalRecord_section_record_details_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.component.MedicalRecordForm
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordScreenState
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.presentation.common.action.AnyaTextAction
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.container.ScreenSection
import se.supernovait.anya.core.presentation.common.modal.NotificationDialog
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel
import se.supernovait.anya.core.presentation.common.text.AnyaIconText
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

/**
 * Composable that lets the users manage their cat's medical record
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun MedicalRecordEntryScreen(
    uiState: MedicalRecordScreenState,
    onEvent: (MedicalRecordScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val medicalRecord = uiState.selectedRecord
    val sectionContentPadding = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)

    if(uiState.showForm) {
        MedicalRecordForm(record = medicalRecord ?: MedicalRecordState.empty, onEvent = onEvent)
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

    ScreenContainer(modifier = modifier) {
        medicalRecord?.let { record ->
            ScreenSection(
                title = stringResource(Res.string.screen_MedicalRecord_section_record_details_title),
                onEdit = { onEvent(MedicalRecordScreenEvent.ShowForm(record)) }
            ) {
                AnyaBoldLabel(text = stringResource(Res.string.screen_MedicalRecord_form_title_label))
                AnyaLabel(text = record.title, modifier = sectionContentPadding)
                AnyaBoldLabel(text = stringResource(Res.string.screen_MedicalRecord_form_description_label))
                AnyaLabel(text = record.description, modifier = sectionContentPadding)
                AnyaBoldLabel(text = stringResource(Res.string.screen_MedicalRecord_form_type_label))
                AnyaLabel(text = record.type.name.lowercase(), modifier = sectionContentPadding)
                AnyaBoldLabel(text = stringResource(Res.string.screen_MedicalRecord_form_date_label))
                AnyaLabel(text = record.date.isoString(), modifier = sectionContentPadding)

                AnyaIconText(
                    text = Res.string.screen_MedicalRecord_form_contagious_label,
                    icon = if(record.contagious) Res.drawable.ic_check else Res.drawable.ic_close,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                )

                AnyaTextAction(
                    label = stringResource(Res.string.delete_action_label, record.type.name.lowercase()),
                    color = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(Res.string.a11y_action_delete_content_description, record.title),
                    onClick = { onEvent(MedicalRecordScreenEvent.ConfirmDeleteRecord(record)) },
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.x2Large)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        MedicalRecordEntryScreen(uiState = MedicalRecordScreenState(selectedRecord = PreviewData.medicalRecord), onEvent = {})
    }
}
