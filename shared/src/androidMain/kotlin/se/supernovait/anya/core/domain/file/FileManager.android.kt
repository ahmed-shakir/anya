package se.supernovait.anya.core.domain.file

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import se.supernovait.anya.core.domain.util.FileUtils

actual class FileManager actual constructor(private val onLaunch: (mimeTypes: List<String>) -> Unit) {
    actual fun launch(mimeTypes: List<String>) {
        onLaunch(mimeTypes)
    }
}

@Composable
actual fun rememberFileManager(filename: String, onResult: (SharedFile?) -> Unit): FileManager {
    val context = LocalContext.current
    val documentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            val docUri = FileUtils.getDocumentUri(context = context, filename = FileUtils.normalize(filename))
            FileUtils.saveFileFromUri(context = context, sourceUri = uri, destinationUri = docUri)

            val sharedFile = SharedFile(contentResolver = context.contentResolver, uri = docUri)
            onResult.invoke(sharedFile)
        }
    }
    return remember {
        FileManager(onLaunch = { mimeTypes ->
            documentLauncher.launch(mimeTypes.toTypedArray())
        })
    }
}

@Composable
actual fun loadFileFromDisk(uri: String?): SharedFile? {
    val context = LocalContext.current
    return uri?.let {
        SharedFile(contentResolver = context.contentResolver, uri = uri.toUri())
    }
}
