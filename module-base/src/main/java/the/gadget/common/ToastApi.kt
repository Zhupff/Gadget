package the.gadget.common

import android.content.Context
import the.gadget.GadgetApplication
import the.gadget.api.globalApi

interface ToastApi {
    companion object {
        val instance: ToastApi by lazy { ToastApi::class.globalApi() }
    }

    fun toastS(msg: String, context: Context = GadgetApplication.instance)

    fun toastL(msg: String, context: Context = GadgetApplication.instance)

    fun singleToastS(msg: String)

    fun singleToastL(msg: String)
}

fun String.toastS(context: Context = GadgetApplication.instance) {
    ToastApi.instance.toastS(this, context)
}

fun String.toastL(context: Context = GadgetApplication.instance) {
    ToastApi.instance.toastL(this, context)
}

fun String.singleToastS() {
    ToastApi.instance.singleToastS(this)
}

fun String.singleToastL() {
    ToastApi.instance.singleToastL(this)
}