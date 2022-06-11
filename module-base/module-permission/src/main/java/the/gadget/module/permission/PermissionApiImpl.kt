package the.gadget.module.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import the.gadget.api.PermissionApi

@AutoService(PermissionApi::class)
class PermissionApiImpl : PermissionApi {

    override fun checkStoragePermission(): Boolean = ActivityCompat
        .checkSelfPermission(ApplicationApi.instance.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

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