package se.supernovait.anya.core.domain.image

import android.content.ContentResolver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import se.supernovait.anya.core.domain.util.BitmapUtils
import se.supernovait.anya.core.domain.util.FileUtils

actual class GalleryManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

@Composable
actual fun rememberGalleryManager(filename: String, onResult: (SharedImage?) -> Unit): GalleryManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val photoUri = FileUtils.getImageUri(context = context, filename = FileUtils.normalize(filename))
            FileUtils.saveFileFromUri(context = context, sourceUri = uri, destinationUri = photoUri)

            val sharedImage = SharedImage(uri = photoUri.toString(), bitmap = BitmapUtils.getBitmapFromUri(photoUri, contentResolver))
            onResult.invoke(sharedImage)
        }
    }
    return remember {
        GalleryManager(onLaunch = {
            galleryLauncher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })
    }
}
