package se.supernovait.anya.app.presentation.owner.component

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
import anya.shared.generated.resources.screen_Owner_form_content_description
import anya.shared.generated.resources.screen_Owner_form_dob_label
import anya.shared.generated.resources.screen_Owner_form_firstname_label
import anya.shared.generated.resources.screen_Owner_form_lastname_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.input_field.AnyaDateField
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.modal.AnyaBottomSheet
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData

@Composable
fun OwnerForm(owner: OwnerState = OwnerState.empty, onEvent: (OwnerScreenEvent) -> Unit) {
    var state by mutableStateOf(owner)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val saveButtonLabel = stringResource(Res.string.save_action_label)

    AnyaBottomSheet(
        contentDescription = stringResource(Res.string.screen_Owner_form_content_description),
        onDismissRequest = { onEvent(OwnerScreenEvent.HideOwnerForm) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            AnyaTextField(
                label = stringResource(Res.string.screen_Owner_form_firstname_label),
                initialValue = owner.firstname,
                onValueChange = { value, _ ->
                    state = state.copy(firstname = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_Owner_form_lastname_label),
                initialValue = owner.lastname,
                onValueChange = { value, _ -> state = state.copy(lastname = value) }
            )

            AnyaDateField(
                label = stringResource(Res.string.screen_Owner_form_dob_label),
                initialValue = owner.dob.isoString(),
                onValueChange = { value, _ -> state = state.copy(dob = value.toLocalDate()) }
            )

            AnyaButton(
                label = saveButtonLabel,
                contentDescription = "$saveButtonLabel $a11yButtonText",
                onClick = { onEvent(OwnerScreenEvent.SaveOwner(state)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.medium)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        OwnerForm(owner = PreviewData.owner, onEvent = { })
    }
}
