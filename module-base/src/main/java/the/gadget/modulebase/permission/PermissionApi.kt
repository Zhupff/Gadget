package the.gadget.modulebase.permission

import android.app.Activity
import java.util.*

interface PermissionApi {
    companion object {
        @JvmStatic
        val instance: PermissionApi by lazy {
            ServiceLoader.load(PermissionApi::class.java).first()
        }
    }

    fun checkStoragePermission(): Boolean

    fun requestStoragePermission(activity: Activity, code: Int)

    fun checkAndRequestStoragePermission(activity: Activity, code: Int, action: Runnable): Boolean
}