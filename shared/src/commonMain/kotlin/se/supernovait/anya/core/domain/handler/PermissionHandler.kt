package se.supernovait.anya.core.domain.handler

import androidx.compose.runtime.Composable
import se.supernovait.anya.core.domain.model.permission.PermissionType

interface PermissionHandler {
    @Composable
    fun askPermission(permission: PermissionType)

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    fun launchSettings()
}
