package se.supernovait.anya.core.domain.handler

import androidx.compose.runtime.Composable
import se.supernovait.anya.core.domain.model.permission.PermissionStatus
import se.supernovait.anya.core.domain.model.permission.PermissionType

expect class DevicePermissionHandler(callback: PermissionCallback) : PermissionHandler {
    @Composable
    override fun askPermission(permission: PermissionType)

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    override fun launchSettings()
}

@Composable
expect fun createPermissionHandler(callback: PermissionCallback): DevicePermissionHandler

interface PermissionCallback {
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus)
}
