package the.gadget.api

import android.app.Activity

interface PermissionApi {
    companion object {
        @JvmStatic
        val instance: PermissionApi by lazy { apiInstance(PermissionApi::class.java) }
    }

    fun checkStoragePermission(): Boolean

    fun requestStoragePermission(activity: Activity, code: Int)

    fun checkAndRequestStoragePermission(activity: Activity, code: Int, action: Runnable): Boolean
}