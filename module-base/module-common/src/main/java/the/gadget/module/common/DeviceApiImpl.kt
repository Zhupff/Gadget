package the.gadget.module.common

import android.content.pm.PackageManager
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import the.gadget.api.DeviceApi
import the.gadget.api.ResourceApi

@AutoService(DeviceApi::class)
class DeviceApiImpl : DeviceApi {

    override fun hasCamera(): Boolean = ApplicationApi.instance.getApplication().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ?: false

    override fun screenWidth(): Int = ResourceApi.instance.getDisplayMetrics().widthPixels

    override fun screenHeight(): Int = ResourceApi.instance.getDisplayMetrics().heightPixels

    override fun screenArea(): Int = screenWidth() * screenHeight()

    override fun screenSides(): Pair<Int, Int> {
        val width = screenWidth()
        val height = screenHeight()
        return if (width <= height) width to height else height to width
    }
}