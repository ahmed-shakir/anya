package se.supernovait.anya.core.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.ic_circle_exclamation
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.profile_image_description
import anya.shared.generated.resources.profile_image_dialog_content_description
import anya.shared.generated.resources.profile_image_permission_cancel_action_label
import anya.shared.generated.resources.profile_image_permission_dialog_content_description
import anya.shared.generated.resources.profile_image_permission_dialog_description
import anya.shared.generated.resources.profile_image_permission_dialog_title
import anya.shared.generated.resources.profile_image_permission_settings_action_label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.domain.file.loadFileFromDisk
import se.supernovait.anya.core.domain.file.rememberFileManager
import se.supernovait.anya.core.domain.handler.PermissionCallback
import se.supernovait.anya.core.domain.handler.createPermissionHandler
import se.supernovait.anya.core.domain.image.rememberGalleryManager
import se.supernovait.anya.core.domain.manager.rememberCameraManager
import se.supernovait.anya.core.domain.model.file.FileSuffix
import se.supernovait.anya.core.domain.model.file.MimeType
import se.supernovait.anya.core.domain.model.permission.PermissionStatus
import se.supernovait.anya.core.domain.model.permission.PermissionType
import se.supernovait.anya.core.domain.util.currentTimeMilliseconds
import se.supernovait.anya.core.presentation.common.modal.ImageSourceOptionDialog
import se.supernovait.anya.core.presentation.common.modal.NotificationDialog
import kotlin.time.Clock

@Composable
fun ProfileImage(
    uri: String? = null,
    filename: String = "profile_image_${Clock.currentTimeMilliseconds()}${FileSuffix.JPEG}",
    placeholder: DrawableResource = Res.drawable.ic_person,
    modifier: Modifier = Modifier,
    description: String = stringResource(Res.string.profile_image_description),
    shape: Shape = RectangleShape,
    size: Dp = 100.dp,
    actionEnabled: Boolean = false,
    onImageSelected: (uri: String?) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val sharedFile = loadFileFromDisk(uri = uri)
    var imageBitmap by remember { mutableStateOf(sharedFile?.toImageBitmap()) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var launchFiles by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }
    var imageSourceOptionDialog by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    val a11yImagePickerDialogText = stringResource(Res.string.profile_image_dialog_content_description)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val a11yImagePickerDialogButtonText = "$a11yImagePickerDialogText $a11yButtonText"

    val permissionsManager = createPermissionHandler(object : PermissionCallback {
        override fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus) {
            when(status) {
                PermissionStatus.GRANTED -> {
                    when(permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
                        PermissionType.FILES -> launchFiles = true
                    }
                }
                else -> {
                    permissionRationalDialog = true
                }
            }
        }
    })

    val cameraManager = rememberCameraManager(filename = filename) { image ->
    coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                onImageSelected(image?.uri())
                image?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }

    val galleryManager = rememberGalleryManager(filename = filename) { image ->
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                onImageSelected(image?.uri())
                image?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }

    val fileManager = rememberFileManager(filename = filename) { image ->
    coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                onImageSelected(image?.uri())
                image?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }

    if(launchCamera) {
        if(permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }

    if(launchGallery) {
        if(permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }

    if(launchFiles) {
        if(permissionsManager.isPermissionGranted(PermissionType.FILES)) {
            fileManager.launch(listOf(MimeType.JPEG))
        } else {
            permissionsManager.askPermission(PermissionType.FILES)
        }
        launchFiles = false
    }

    if(launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }

    if(imageSourceOptionDialog) {
        ImageSourceOptionDialog(
            onDismissRequest = {
                imageSourceOptionDialog = false
            }, onCameraRequest = {
                imageSourceOptionDialog = false
                launchCamera = true
            }, onGalleryRequest = {
                imageSourceOptionDialog = false
                launchGallery = true
            }, onFileRequest = {
                imageSourceOptionDialog = false
                launchFiles = true
            }
        )
    }

    if(permissionRationalDialog) {
        NotificationDialog(
            title = stringResource(Res.string.profile_image_permission_dialog_title),
            text = stringResource(Res.string.profile_image_permission_dialog_description),
            contentDescription = stringResource(Res.string.profile_image_permission_dialog_content_description),
            icon = Res.drawable.ic_circle_exclamation,
            actionLabel = Res.string.profile_image_permission_settings_action_label,
            dismissLabel = Res.string.profile_image_permission_cancel_action_label,
            onAction = {
                permissionRationalDialog = false
                launchSetting = true
            },
            onDismissRequest = {
                permissionRationalDialog = false
            }
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.onSurface)
            .clickable(enabled = actionEnabled) {
                imageSourceOptionDialog = true
            }
            .semantics { this.contentDescription = a11yImagePickerDialogButtonText }
    ) {
        if(imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentScale = ContentScale.Crop,
                contentDescription = description,
                modifier = Modifier.size(size)
            )
        } else {
            Image(
                painter = painterResource(placeholder),
                contentDescription = description,
                modifier = Modifier
                    .size(size)
                    .padding(MaterialTheme.spacing.extraSmall)
            )
        }
    }
}
