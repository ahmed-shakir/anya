package se.supernovait.anya.app.fakes

import androidx.compose.runtime.Composable
import se.supernovait.anya.core.domain.handler.PermissionHandler
import se.supernovait.anya.core.domain.model.permission.PermissionType

class FakePermissionHandler : PermissionHandler {
    var askedPermission: PermissionType? = null
    var grantedPermissions = mutableSetOf<PermissionType>()

    @Composable
    override fun askPermission(permission: PermissionType) {
        askedPermission = permission
    }

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean = permission in grantedPermissions

    @Composable
    override fun launchSettings() {}
}
