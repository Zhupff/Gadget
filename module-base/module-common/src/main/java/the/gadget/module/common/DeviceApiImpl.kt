package the.gadget.module.common

import android.content.pm.PackageManager
import the.gadget.GadgetApplication
import the.gadget.api.GlobalApi
import the.gadget.common.DeviceApi
import the.gadget.common.ResourceApi

@GlobalApi(DeviceApi::class)
class DeviceApiImpl : DeviceApi {

    override fun hasCamera(): Boolean = GadgetApplication.instance.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ?: false

    override fun screenWidth(): Int = ResourceApi.getDisplayMetrics().widthPixels

    override fun screenHeight(): Int = ResourceApi.getDisplayMetrics().heightPixels

    override fun screenArea(): Int = screenWidth() * screenHeight()

    override fun screenSides(): Pair<Int, Int> {
        val width = screenWidth()
        val height = screenHeight()
        return if (width <= height) width to height else height to width
    }
}