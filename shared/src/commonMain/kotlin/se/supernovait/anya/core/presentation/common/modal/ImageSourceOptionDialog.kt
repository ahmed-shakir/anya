package se.supernovait.anya.core.presentation.common.modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.ic_camera
import anya.shared.generated.resources.ic_file
import anya.shared.generated.resources.ic_images
import anya.shared.generated.resources.profile_image_dialog_camera_action_content_description
import anya.shared.generated.resources.profile_image_dialog_camera_action_label
import anya.shared.generated.resources.profile_image_dialog_content_description
import anya.shared.generated.resources.profile_image_dialog_files_action_content_description
import anya.shared.generated.resources.profile_image_dialog_files_action_label
import anya.shared.generated.resources.profile_image_dialog_photo_library_action_content_description
import anya.shared.generated.resources.profile_image_dialog_photo_library_action_label
import anya.shared.generated.resources.profile_image_title
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun ImageSourceOptionDialog(
    onDismissRequest: () -> Unit,
    onCameraRequest: () -> Unit = {},
    onGalleryRequest: () -> Unit = {},
    onFileRequest: (() -> Unit)? = null
) {
    val a11yImagePickerDialogText = stringResource(Res.string.profile_image_dialog_content_description)
    val a11yCameraButtonText = stringResource(Res.string.profile_image_dialog_camera_action_content_description)
    val a11yPhotoLibraryButtonText = stringResource(Res.string.profile_image_dialog_photo_library_action_content_description)
    val a11yFilesButtonText = stringResource(Res.string.profile_image_dialog_files_action_content_description)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .semantics { this.contentDescription = a11yImagePickerDialogText }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(MaterialTheme.spacing.large)
            ) {
                Text(
                    text = stringResource(Res.string.profile_image_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.medium)
                        .clickable { onCameraRequest.invoke() }
                        .semantics { this.contentDescription = a11yCameraButtonText }
                ) {
                    AnyaIcon(icon = Res.drawable.ic_camera, size = AnyaActionDefaults.defaultStyle.iconSize)
                    AnyaBoldLabel(text = stringResource(Res.string.profile_image_dialog_camera_action_label))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.medium)
                        .clickable { onGalleryRequest.invoke() }
                        .semantics { this.contentDescription = a11yPhotoLibraryButtonText }
                ) {
                    AnyaIcon(icon = Res.drawable.ic_images, size = AnyaActionDefaults.defaultStyle.iconSize)
                    AnyaBoldLabel(text = stringResource(Res.string.profile_image_dialog_photo_library_action_label))
                }

                onFileRequest?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.medium)
                            .clickable { it.invoke() }
                            .semantics { this.contentDescription = a11yFilesButtonText }
                    ) {
                        AnyaIcon(icon = Res.drawable.ic_file, size = AnyaActionDefaults.defaultStyle.iconSize)
                        AnyaBoldLabel(text = stringResource(Res.string.profile_image_dialog_files_action_label))
                    }
                }
            }
        }
    }
}
