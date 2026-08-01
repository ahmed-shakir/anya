package se.supernovait.anya.app.presentation.medical_record.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.save_action_label
import anya.shared.generated.resources.screen_MedicalRecord_form_contagious_label
import anya.shared.generated.resources.screen_MedicalRecord_form_content_description
import anya.shared.generated.resources.screen_MedicalRecord_form_date_label
import anya.shared.generated.resources.screen_MedicalRecord_form_description_label
import anya.shared.generated.resources.screen_MedicalRecord_form_title_label
import anya.shared.generated.resources.screen_MedicalRecord_form_type_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.domain.model.MedicalRecordType
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.core.domain.model.SelectFieldOption
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.input_field.AnyaCheckbox
import se.supernovait.anya.core.presentation.common.input_field.AnyaDateField
import se.supernovait.anya.core.presentation.common.input_field.AnyaSelectField
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.modal.AnyaBottomSheet
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData

@Composable
fun MedicalRecordForm(record: MedicalRecordState = MedicalRecordState.empty, onEvent: (MedicalRecordScreenEvent) -> Unit) {
    var state by mutableStateOf(record)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val saveButtonLabel = stringResource(Res.string.save_action_label)

    AnyaBottomSheet(
        contentDescription = stringResource(Res.string.screen_MedicalRecord_form_content_description),
        onDismissRequest = { onEvent(MedicalRecordScreenEvent.HideForm) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            AnyaSelectField(
                label = stringResource(Res.string.screen_MedicalRecord_form_type_label),
                initialValue = record.type.name,
                options = MedicalRecordType.entries.map {
                    SelectFieldOption(id = it.ordinal.toLong(), value = it.name)
                },
                onValueChange = { option -> state = state.copy(type = MedicalRecordType.ordinalOf(option.id.toInt())) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_MedicalRecord_form_title_label),
                initialValue = record.title,
                onValueChange = { value, _ -> state = state.copy(title = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_MedicalRecord_form_description_label),
                initialValue = record.description,
                maxChar = 250,
                onValueChange = { value, _ -> state = state.copy(description = value) }
            )

            AnyaDateField(
                label = stringResource(Res.string.screen_MedicalRecord_form_date_label),
                initialValue = record.date.isoString(),
                onValueChange = { value, _ -> state = state.copy(date = value.toLocalDate()) }
            )

            AnyaCheckbox(
                label = stringResource(Res.string.screen_MedicalRecord_form_contagious_label),
                initialValue = record.contagious,
                onCheckedChange = { checked -> state = state.copy(contagious = checked) }
            )

            AnyaButton(
                label = saveButtonLabel,
                contentDescription = "$saveButtonLabel $a11yButtonText",
                onClick = { onEvent(MedicalRecordScreenEvent.SaveRecord(state)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.medium)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        MedicalRecordForm(record = PreviewData.medicalRecord, onEvent = { })
    }
}
