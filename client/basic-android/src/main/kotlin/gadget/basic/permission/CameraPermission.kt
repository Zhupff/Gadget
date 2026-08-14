package gadget.basic.permission

import android.Manifest
import androidx.fragment.app.Fragment

fun Fragment.registerCameraPermission(
    onResult: (granted: Boolean) -> Unit,
): PermissionRequest = registerPermission(Manifest.permission.CAMERA, onResult)
