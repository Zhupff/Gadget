package the.gadget.module.common

import android.content.pm.PackageManager
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import the.gadget.api.DeviceApi

@AutoService(DeviceApi::class)
class DeviceApiImpl : DeviceApi {

    override fun hasCamera(): Boolean = ApplicationApi.instance.getApplication().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ?: false
}