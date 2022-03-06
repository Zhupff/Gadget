package the.gadget.modulebase.permission

import android.app.Activity
import the.gadget.modulebase.api.apiInstance

interface PermissionApi {
    companion object {
        @JvmStatic
        val instance: PermissionApi by lazy { apiInstance(PermissionApi::class.java) }
    }

    fun checkStoragePermission(): Boolean

    fun requestStoragePermission(activity: Activity, code: Int)

    fun checkAndRequestStoragePermission(activity: Activity, code: Int, action: Runnable): Boolean
}