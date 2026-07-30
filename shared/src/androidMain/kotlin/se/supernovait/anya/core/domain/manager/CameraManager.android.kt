package se.supernovait.anya.core.domain.manager

import android.content.ContentResolver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import se.supernovait.anya.core.domain.image.SharedImage
import se.supernovait.anya.core.domain.util.BitmapUtils
import se.supernovait.anya.core.domain.util.FileUtils

actual class CameraManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

@Composable
actual fun rememberCameraManager(filename: String, onResult: (SharedImage?) -> Unit): CameraManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    val photoUri = FileUtils.getImageUri(context, FileUtils.normalize(filename))

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onResult.invoke(SharedImage(photoUri.toString(), BitmapUtils.getBitmapFromUri(photoUri, contentResolver)))
            }
        }
    )
    return remember {
        CameraManager(
            onLaunch = {
                cameraLauncher.launch(photoUri)
            }
        )
    }
}
