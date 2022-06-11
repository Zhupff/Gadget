package the.gadget.api

interface ToastApi {
    companion object {
        val instance: ToastApi by lazy { apiInstance(ToastApi::class.java) }
    }

    fun toast(msg: String)

    fun longToast(msg: String)
}