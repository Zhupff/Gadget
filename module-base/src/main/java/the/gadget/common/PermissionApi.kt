package the.gadget.common

import android.app.Activity
import the.gadget.api.globalApi

interface PermissionApi {
    companion object {
        @JvmStatic
        val instance: PermissionApi by lazy { PermissionApi::class.globalApi() }
    }

    fun checkStoragePermission(): Boolean

    fun requestStoragePermission(activity: Activity, code: Int)

    fun checkAndRequestStoragePermission(activity: Activity, code: Int, action: Runnable): Boolean
}