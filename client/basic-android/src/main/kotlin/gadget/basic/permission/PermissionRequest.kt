package gadget.basic.permission

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PermissionRequest internal constructor(
    private val permission: String,
    private val launcher: ActivityResultLauncher<String>,
) {
    fun request() {
        launcher.launch(permission)
    }
}

fun Fragment.registerPermission(
    permission: String,
    onResult: (granted: Boolean) -> Unit,
): PermissionRequest = PermissionRequest(
    permission = permission,
    launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        onResult(granted)
    },
)
