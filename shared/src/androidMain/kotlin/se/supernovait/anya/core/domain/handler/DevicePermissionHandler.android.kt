package se.supernovait.anya.core.domain.handler

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import se.supernovait.anya.core.domain.model.permission.PermissionStatus
import se.supernovait.anya.core.domain.model.permission.PermissionType

actual class DevicePermissionHandler actual constructor(private val callback: PermissionCallback) : PermissionHandler {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    actual override fun askPermission(permission: PermissionType) {
        val lifecycleOwner = LocalLifecycleOwner.current
        when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                LaunchedEffect(cameraPermissionState) {
                    val permissionResult = cameraPermissionState.status
                    if (!permissionResult.isGranted) {
                        if (permissionResult.shouldShowRationale) {
                            callback.onPermissionStatus(permission, PermissionStatus.SHOW_RATIONAL)
                        } else {
                            lifecycleOwner.lifecycleScope.launch {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                    } else {
                        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                    }
                }
            }

            PermissionType.FILES -> {
                // Granted by default because in Android GetContent API does not require any runtime permissions
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }

            PermissionType.GALLERY -> {
                // Granted by default because in Android GetContent API does not require any runtime permissions
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                cameraPermissionState.status.isGranted
            }

            PermissionType.FILES -> {
                // Granted by default because in Android GetContent API does not require any runtime permissions
                true
            }

            PermissionType.GALLERY -> {
                // Granted by default because in Android GetContent API does not require any runtime permissions
                true
            }
        }
    }

    @Composable
    actual override fun launchSettings() {
        val context = LocalContext.current
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }
}

@Composable
actual fun createPermissionHandler(callback: PermissionCallback): DevicePermissionHandler {
    return remember { DevicePermissionHandler(callback) }
}
