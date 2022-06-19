package the.gadget.api

interface DeviceApi {
    companion object {
        val instance: DeviceApi by lazy { apiInstance(DeviceApi::class.java) }
    }

    fun hasCamera(): Boolean

    fun screenWidth(): Int

    fun screenHeight(): Int

    fun screenArea(): Int

    fun screenSides(): Pair<Int, Int>
}