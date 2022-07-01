package nz.co.warehouseandroidtest

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionChecker(var context: Context) {
    fun ifLackPermissions(permissions: Array<String>): Boolean {
        var i = 0
        while (i < permissions.size) {
            if (ifLackPermission(permissions[i])) {
                return true
            }
            i++
        }
        return false
    }

    fun ifLackPermission(permission: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission!!
        ) == PackageManager.PERMISSION_DENIED
    }
}