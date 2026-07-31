package se.supernovait.anya.app.presentation.owner.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.address_city_label
import anya.shared.generated.resources.address_country_label
import anya.shared.generated.resources.address_county_label
import anya.shared.generated.resources.address_postal_code_label
import anya.shared.generated.resources.address_street_label
import anya.shared.generated.resources.auth_action_sign_out_label
import anya.shared.generated.resources.screen_Owner_form_dob_label
import anya.shared.generated.resources.screen_Owner_form_full_name_label
import anya.shared.generated.resources.screen_Owner_form_username_label
import anya.shared.generated.resources.screen_Owner_image_description
import anya.shared.generated.resources.screen_Owner_section_address_title
import anya.shared.generated.resources.screen_Owner_section_personal_details_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.address.AddressForm
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.component.OwnerForm
import se.supernovait.anya.app.presentation.owner.state.OwnerScreenState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.presentation.common.ProfileImage
import se.supernovait.anya.core.presentation.common.action.AnyaTextAction
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.container.ScreenSection
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

/**
 * Composable that lets the users manage cat owners
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun OwnerProfileScreen(
    uiState: OwnerScreenState,
    onEvent: (OwnerScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val owner = uiState.selectedOwner
    val sectionContentPadding = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val signOutButtonLabel = stringResource(Res.string.auth_action_sign_out_label)

    if(uiState.showOwnerForm) {
        OwnerForm(owner = owner ?: OwnerState.empty, onEvent = onEvent)
    }

    if(uiState.showAddressForm) {
        AddressForm(
            owner = owner ?: OwnerState.empty,
            address = owner?.address ?: AddressState.empty,
            onEvent = onEvent
        )
    }

    ScreenContainer(modifier = modifier) {
        owner?.let { owner ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                ProfileImage(
                    uri = owner.imageUri,
                    filename = "${owner.firstname}_${owner.lastname}_${owner.dob}",
                    description = stringResource(Res.string.screen_Owner_image_description),
                    shape = CircleShape,
                    size = 160.dp,
                    actionEnabled = true,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.large),
                    onImageSelected = { uri -> onEvent(OwnerScreenEvent.SaveOwner(owner.copy(imageUri = uri))) }
                )

                ScreenSection(
                    title = stringResource(Res.string.screen_Owner_section_personal_details_title),
                    onEdit = { onEvent(OwnerScreenEvent.ShowOwnerForm(owner)) }
                ) {
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Owner_form_full_name_label))
                    AnyaLabel(text = owner.name, modifier = sectionContentPadding)
                    owner.username?.let { username ->
                        AnyaBoldLabel(text = stringResource(Res.string.screen_Owner_form_username_label))
                        AnyaLabel(text = username, modifier = sectionContentPadding)
                    }
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Owner_form_dob_label))
                    AnyaLabel(text = owner.dob.isoString(), modifier = sectionContentPadding)
                }

                ScreenSection(
                    title = stringResource(Res.string.screen_Owner_section_address_title),
                    onEdit = { onEvent(OwnerScreenEvent.ShowAddressForm(owner)) }
                ) {
                    owner.address?.let { address ->
                        AnyaBoldLabel(text = stringResource(Res.string.address_street_label))
                        AnyaLabel(text = address.street, modifier = sectionContentPadding)
                        if(address.postalCode.isNotBlank()) {
                            AnyaBoldLabel(text = stringResource(Res.string.address_postal_code_label))
                            AnyaLabel(text = address.postalCode, modifier = sectionContentPadding)
                        }
                        AnyaBoldLabel(text = stringResource(Res.string.address_city_label))
                        AnyaLabel(text = address.city, modifier = sectionContentPadding)
                        AnyaBoldLabel(text = stringResource(Res.string.address_county_label))
                        AnyaLabel(text = address.county, modifier = sectionContentPadding)
                        AnyaBoldLabel(text = stringResource(Res.string.address_country_label))
                        AnyaLabel(text = address.country)
                    }
                }

                AnyaTextAction(
                    label = signOutButtonLabel,
                    contentDescription = "$signOutButtonLabel $a11yButtonText",
                    color = MaterialTheme.colorScheme.error,
                    onClick = { onEvent(OwnerScreenEvent.SignOut) },
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
        OwnerProfileScreen(uiState = OwnerScreenState(selectedOwner = PreviewData.owner), onEvent = {})
    }
}
