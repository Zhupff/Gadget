package the.gadget.api

import android.content.Context

interface ToastApi {
    companion object {
        val instance: ToastApi by lazy { apiInstance(ToastApi::class.java) }
    }

    fun toastS(msg: String, context: Context = ApplicationApi.instance.getApplication())

    fun toastL(msg: String, context: Context = ApplicationApi.instance.getApplication())

    fun singleToastS(msg: String)

    fun singleToastL(msg: String)
}

fun String.toastS(context: Context = ApplicationApi.instance.getApplication()) {
    ToastApi.instance.toastS(this, context)
}

fun String.toastL(context: Context = ApplicationApi.instance.getApplication()) {
    ToastApi.instance.toastL(this, context)
}

fun String.singleToastS() {
    ToastApi.instance.singleToastS(this)
}

fun String.singleToastL() {
    ToastApi.instance.singleToastL(this)
}