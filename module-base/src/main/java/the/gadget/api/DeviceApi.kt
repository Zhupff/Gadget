package the.gadget.api

interface DeviceApi {
    companion object {
        val instance: DeviceApi by lazy { apiInstance(DeviceApi::class.java) }
    }

    fun hasCamera(): Boolean
}