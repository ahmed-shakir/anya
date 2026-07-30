package se.supernovait.anya.core.domain.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import se.supernovait.anya.core.domain.image.SharedImage
import se.supernovait.anya.core.domain.util.FileUtils

actual class CameraManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

@Composable
actual fun rememberCameraManager(filename: String, onResult: (SharedImage?) -> Unit): CameraManager {
    val imagePicker = UIImagePickerController()
    val cameraDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
            override fun imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>) {
                val image =
                    didFinishPickingMediaWithInfo.getValue(UIImagePickerControllerEditedImage) as? UIImage
                        ?: didFinishPickingMediaWithInfo.getValue(UIImagePickerControllerOriginalImage) as? UIImage

                image?.let { img ->
                    val filePath = FileUtils.getFileUri(filename = FileUtils.normalize(filename))
                    // Convert image to JPEG data
                    val data = UIImageJPEGRepresentation(img, SharedImage.COMPRESSION_QUALITY)
                    FileUtils.saveFileFromUri(filePath = filePath, data = data)
                    onResult.invoke(SharedImage(filePath, img))
                }
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }
    return remember {
        CameraManager {
            imagePicker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)
            imagePicker.setAllowsEditing(true)
            imagePicker.setCameraCaptureMode(UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto)
            imagePicker.setDelegate(cameraDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(imagePicker, true, null)
        }
    }
}
