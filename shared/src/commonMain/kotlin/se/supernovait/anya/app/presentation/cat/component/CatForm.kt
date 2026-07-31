package se.supernovait.anya.app.presentation.cat.component

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
import anya.shared.generated.resources.screen_Cat_form_address_label
import anya.shared.generated.resources.screen_Cat_form_breed_label
import anya.shared.generated.resources.screen_Cat_form_content_description
import anya.shared.generated.resources.screen_Cat_form_dob_label
import anya.shared.generated.resources.screen_Cat_form_eye_color_label
import anya.shared.generated.resources.screen_Cat_form_fur_color_label
import anya.shared.generated.resources.screen_Cat_form_name_label
import anya.shared.generated.resources.screen_Cat_form_nickname_label
import anya.shared.generated.resources.screen_Cat_form_owner_label
import anya.shared.generated.resources.screen_Cat_form_sterilized_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.input_field.AnyaCheckbox
import se.supernovait.anya.core.presentation.common.input_field.AnyaDateField
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.modal.AnyaBottomSheet
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData

@Composable
fun CatForm(cat: CatState = CatState.empty, onEvent: (CatScreenEvent) -> Unit) {
    var state by mutableStateOf(cat)
    var isCurrentUserOwner by mutableStateOf(false)
    var useOwnerAddress by mutableStateOf(false)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val saveButtonLabel = stringResource(Res.string.save_action_label)

    AnyaBottomSheet(
        contentDescription = stringResource(Res.string.screen_Cat_form_content_description),
        onDismissRequest = { onEvent(CatScreenEvent.HideCatForm) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            AnyaTextField(
                label = stringResource(Res.string.screen_Cat_form_name_label),
                initialValue = cat.name,
                onValueChange = { value, _ -> state = state.copy(name = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_Cat_form_nickname_label),
                initialValue = cat.nickname,
                onValueChange = { value, _ -> state = state.copy(nickname = value) }
            )

            AnyaDateField(
                label = stringResource(Res.string.screen_Cat_form_dob_label),
                initialValue = cat.dob.isoString(),
                onValueChange = { value, _ -> state = state.copy(dob = value.toLocalDate()) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_Cat_form_breed_label),
                initialValue = cat.breed,
                onValueChange = { value, _ -> state = state.copy(breed = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_Cat_form_eye_color_label),
                initialValue = cat.eyeColor,
                onValueChange = { value, _ -> state = state.copy(eyeColor = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.screen_Cat_form_fur_color_label),
                initialValue = cat.furColor,
                onValueChange = { value, _ -> state = state.copy(furColor = value) }
            )

            AnyaCheckbox(
                label = stringResource(Res.string.screen_Cat_form_sterilized_label),
                initialValue = cat.sterilized,
                onCheckedChange = { checked -> state = state.copy(sterilized = checked) }
            )

            if(cat.ownerId == null) {
                AnyaCheckbox(
                    label = stringResource(Res.string.screen_Cat_form_owner_label),
                    onCheckedChange = { checked -> isCurrentUserOwner = checked }
                )
            }

            if(cat.address == null) {
                AnyaCheckbox(
                    label = stringResource(Res.string.screen_Cat_form_address_label),
                    onCheckedChange = { checked -> useOwnerAddress = checked }
                )
            }

            AnyaButton(
                label = saveButtonLabel,
                contentDescription = "$saveButtonLabel $a11yButtonText",
                onClick = { onEvent(CatScreenEvent.SaveCat(state, isCurrentUserOwner, useOwnerAddress)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.medium)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        CatForm(cat = PreviewData.cat, onEvent = { })
    }
}
