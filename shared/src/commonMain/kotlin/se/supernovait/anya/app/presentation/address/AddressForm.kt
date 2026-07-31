package se.supernovait.anya.app.presentation.address

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
import anya.shared.generated.resources.address_city_label
import anya.shared.generated.resources.address_country_label
import anya.shared.generated.resources.address_county_label
import anya.shared.generated.resources.address_form_content_description
import anya.shared.generated.resources.address_postal_code_label
import anya.shared.generated.resources.address_street_label
import anya.shared.generated.resources.save_action_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.action.AnyaButton
import se.supernovait.anya.core.presentation.common.input_field.AnyaTextField
import se.supernovait.anya.core.presentation.common.modal.AnyaBottomSheet
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData

@Composable
fun AddressForm(address: AddressState, onSaveRequest: (AddressState) -> Unit, onDismissRequest: () -> Unit) {
    var state by mutableStateOf(address)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val saveButtonLabel = stringResource(Res.string.save_action_label)

    AnyaBottomSheet(
        contentDescription = stringResource(Res.string.address_form_content_description),
        onDismissRequest = onDismissRequest
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            AnyaTextField(
                label = stringResource(Res.string.address_street_label),
                initialValue = address.street,
                onValueChange = { value, _ -> state = state.copy(street = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.address_postal_code_label),
                initialValue = address.postalCode,
                onValueChange = { value, _ -> state = state.copy(postalCode = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.address_city_label),
                initialValue = address.city,
                onValueChange = { value, _ -> state = state.copy(city = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.address_county_label),
                initialValue = address.county,
                onValueChange = { value, _ -> state = state.copy(county = value) }
            )

            AnyaTextField(
                label = stringResource(Res.string.address_country_label),
                initialValue = address.country,
                onValueChange = { value, _ -> state = state.copy(country = value) }
            )

            AnyaButton(
                label = saveButtonLabel,
                contentDescription = "$saveButtonLabel $a11yButtonText",
                onClick = { onSaveRequest(address) },
                modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.medium)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        AddressForm(address = PreviewData.address, onSaveRequest = { }, onDismissRequest = { })
    }
}
