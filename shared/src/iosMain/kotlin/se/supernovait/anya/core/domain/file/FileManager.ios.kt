package se.supernovait.anya.core.domain.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.darwin.NSObject
import se.supernovait.anya.core.domain.util.FileUtils

actual class FileManager actual constructor(private val onLaunch: (mimeTypes: List<String>) -> Unit) {
    actual fun launch(mimeTypes: List<String>) {
        onLaunch(mimeTypes)
    }
}

@Composable
actual fun rememberFileManager(filename: String, onResult: (SharedFile?) -> Unit): FileManager {
    val documentDelegate = remember {
        object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentAtURL: NSURL) {
                val filePath = FileUtils.getFileUri(filename = FileUtils.normalize(filename))
                val data = didPickDocumentAtURL.dataRepresentation()
                FileUtils.saveFileFromUri(filePath = filePath, data = data)
                onResult.invoke(SharedFile(filePath))
                controller.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        FileManager(onLaunch = {  mimeTypes ->
            val documentPicker = UIDocumentPickerViewController(
                documentTypes = mimeTypes,
                inMode = UIDocumentPickerMode.UIDocumentPickerModeOpen
            )
            documentPicker.setDelegate(documentDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(documentPicker, true, null)
        })
    }
}

@Composable
actual fun loadFileFromDisk(uri: String?): SharedFile? {
    return uri?.let {
        SharedFile(uri = uri)
    }
}
