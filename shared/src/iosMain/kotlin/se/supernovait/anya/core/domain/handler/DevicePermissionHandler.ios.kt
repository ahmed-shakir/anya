package se.supernovait.anya.core.domain.handler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import se.supernovait.anya.core.domain.model.permission.PermissionStatus
import se.supernovait.anya.core.domain.model.permission.PermissionType

actual class DevicePermissionHandler actual constructor(private val callback: PermissionCallback) : PermissionHandler {

    @Composable
    actual override fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.CAMERA -> {
                val status: AVAuthorizationStatus = remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                askCameraPermission(status, permission, callback)
            }

            PermissionType.FILES -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }

            PermissionType.GALLERY -> {
                val status: PHAuthorizationStatus = remember { PHPhotoLibrary.authorizationStatus() }
                askGalleryPermission(status, permission, callback)
            }
        }
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val status: AVAuthorizationStatus = remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                status == AVAuthorizationStatusAuthorized
            }

            PermissionType.FILES -> true

            PermissionType.GALLERY -> {
                val status: PHAuthorizationStatus = remember { PHPhotoLibrary.authorizationStatus() }
                status == PHAuthorizationStatusAuthorized
            }
        }
    }

    @Composable
    actual override fun launchSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}

@Composable
actual fun createPermissionHandler(callback: PermissionCallback): DevicePermissionHandler {
    return DevicePermissionHandler(callback)
}


private fun askCameraPermission(status: AVAuthorizationStatus, permission: PermissionType, callback: PermissionCallback) {
    when (status) {
        AVAuthorizationStatusAuthorized -> callback.onPermissionStatus(permission, PermissionStatus.GRANTED)

        AVAuthorizationStatusNotDetermined -> {
            return AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { isGranted ->
                if(isGranted) callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                else callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }
        }

        AVAuthorizationStatusDenied -> callback.onPermissionStatus(permission, PermissionStatus.DENIED)

        else -> error("unknown camera status $status")
    }
}

private fun askGalleryPermission(status: PHAuthorizationStatus, permission: PermissionType, callback: PermissionCallback) {
    when (status) {
        PHAuthorizationStatusAuthorized -> callback.onPermissionStatus(permission, PermissionStatus.GRANTED)

        PHAuthorizationStatusNotDetermined -> {
            PHPhotoLibrary.requestAuthorization { newStatus ->
                askGalleryPermission(newStatus, permission, callback)
            }
        }

        PHAuthorizationStatusDenied -> callback.onPermissionStatus(permission, PermissionStatus.DENIED)

        else -> error("unknown gallery status $status")
    }
}
