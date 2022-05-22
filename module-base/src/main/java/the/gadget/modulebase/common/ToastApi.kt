package the.gadget.modulebase.common

import the.gadget.modulebase.api.apiInstance

interface ToastApi {
    companion object {
        val instance: ToastApi by lazy { apiInstance(ToastApi::class.java) }
    }

    fun toast(msg: String)

    fun longToast(msg: String)
}