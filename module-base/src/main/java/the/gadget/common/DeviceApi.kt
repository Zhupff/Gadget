package the.gadget.common

import the.gadget.api.globalApi

interface DeviceApi {
    companion object : DeviceApi by DeviceApi::class.globalApi()

    fun hasCamera(): Boolean

    fun screenWidth(): Int

    fun screenHeight(): Int

    fun screenArea(): Int

    fun screenSides(): Pair<Int, Int>
}