package se.supernovait.anya.app.presentation.cat.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_dialog_delete_confirmation_content_description
import anya.shared.generated.resources.address_city_label
import anya.shared.generated.resources.address_country_label
import anya.shared.generated.resources.address_county_label
import anya.shared.generated.resources.address_postal_code_label
import anya.shared.generated.resources.address_street_label
import anya.shared.generated.resources.dialog_delete_confirmation_message
import anya.shared.generated.resources.dialog_delete_confirmation_title
import anya.shared.generated.resources.ic_check
import anya.shared.generated.resources.ic_close
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.screen_Cat_form_breed_label
import anya.shared.generated.resources.screen_Cat_form_dob_label
import anya.shared.generated.resources.screen_Cat_form_eye_color_label
import anya.shared.generated.resources.screen_Cat_form_fur_color_label
import anya.shared.generated.resources.screen_Cat_form_name_label
import anya.shared.generated.resources.screen_Cat_form_nickname_label
import anya.shared.generated.resources.screen_Cat_form_sterilized_label
import anya.shared.generated.resources.screen_Cat_image_description
import anya.shared.generated.resources.screen_Cat_section_address_title
import anya.shared.generated.resources.screen_Cat_section_owner_title
import anya.shared.generated.resources.screen_Cat_section_personal_details_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.address.AddressForm
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.component.CatForm
import se.supernovait.anya.app.presentation.cat.state.CatScreenState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.presentation.common.ProfileImage
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
 * Composable that lets the users manage their cat
 * @param uiState the screen UI state
 * @param onEvent lambda that triggers different actions
 */
@Composable
fun CatProfileScreen(
    uiState: CatScreenState,
    onEvent: (CatScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val cat = uiState.selectedCat
    val sectionContentPadding = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)

    if(uiState.showCatForm) {
        CatForm(cat = cat ?: CatState.empty, onEvent = onEvent)
    }

    if (uiState.showAddressForm) {
        AddressForm(
            address = cat?.address ?: AddressState.empty,
            onSaveRequest = { address ->
                val catId = cat?.id ?: CatState.empty.id
                onEvent(CatScreenEvent.SaveAddress(catId = catId, address = address))
            },
            onDismissRequest = { onEvent(CatScreenEvent.HideAddressForm) },
        )
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

    ScreenContainer(modifier = modifier) {
        cat?.let { cat ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                ProfileImage(
                    uri = cat.imageUri,
                    filename = "${cat.name}_${cat.dob}",
                    description = stringResource(Res.string.screen_Cat_image_description),
                    shape = CircleShape,
                    size = 160.dp,
                    actionEnabled = true,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.large),
                    onImageSelected = { uri -> onEvent(CatScreenEvent.SaveCat(cat.copy(imageUri = uri))) }
                )

                ScreenSection(
                    title = stringResource(Res.string.screen_Cat_section_personal_details_title),
                    onEdit = { onEvent(CatScreenEvent.ShowCatForm(cat)) }
                ) {
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Cat_form_name_label))
                    AnyaLabel(text = cat.name, modifier = sectionContentPadding)
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Cat_form_nickname_label))
                    AnyaLabel(text = cat.nickname, modifier = sectionContentPadding)
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Cat_form_dob_label))
                    AnyaLabel(text = cat.dob.isoString(), modifier = sectionContentPadding)
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Cat_form_breed_label))
                    AnyaLabel(text = cat.breed, modifier = sectionContentPadding)
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Cat_form_eye_color_label))
                    AnyaLabel(text = cat.eyeColor, modifier = sectionContentPadding)
                    AnyaBoldLabel(text = stringResource(Res.string.screen_Cat_form_fur_color_label))
                    AnyaLabel(text = cat.furColor, modifier = sectionContentPadding)

                    AnyaIconText(
                        text = Res.string.screen_Cat_form_sterilized_label,
                        icon = if(cat.sterilized) Res.drawable.ic_check else Res.drawable.ic_close,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                    )
                }

                ScreenSection(
                    title = stringResource(Res.string.screen_Cat_section_address_title),
                    onEdit = { onEvent(CatScreenEvent.ShowAddressForm(cat)) }
                ) {
                    cat.address?.let { address ->
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

                ScreenSection(title = stringResource(Res.string.screen_Cat_section_owner_title)) {
                    cat.owner?.let {
                        AnyaTextAction(
                            label = "${it.firstname} ${it.lastname}",
                            color = MaterialTheme.colorScheme.onSurface,
                            onClick = { onEvent(CatScreenEvent.NavigateToOwner(it.id)) }
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        CatProfileScreen(uiState = CatScreenState(selectedCat = PreviewData.cat), onEvent = {})
    }
}
