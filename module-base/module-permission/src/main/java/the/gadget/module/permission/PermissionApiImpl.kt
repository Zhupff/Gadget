package the.gadget.module.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import the.gadget.GadgetApplication
import the.gadget.api.GlobalApi
import the.gadget.common.PermissionApi

@GlobalApi(PermissionApi::class)
class PermissionApiImpl : PermissionApi {

    override fun checkStoragePermission(): Boolean = ActivityCompat
        .checkSelfPermission(GadgetApplication.instance, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    override fun requestStoragePermission(activity: Activity, code: Int) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), code)
        }
    }

    override fun checkAndRequestStoragePermission(activity: Activity, code: Int, action: Runnable): Boolean {
        return checkStoragePermission().also {
            if (!it)
                requestStoragePermission(activity, code)
            else
                action.run()
        }
    }
}