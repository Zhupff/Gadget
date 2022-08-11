package the.gadget.common

import android.content.Context
import the.gadget.GadgetApplication
import the.gadget.api.globalApi

interface ToastApi {
    companion object : ToastApi by ToastApi::class.globalApi()

    fun toastS(msg: String, context: Context = GadgetApplication.instance)

    fun toastL(msg: String, context: Context = GadgetApplication.instance)

    fun singleToastS(msg: String)

    fun singleToastL(msg: String)
}

fun String.toastS(context: Context = GadgetApplication.instance) {
    ToastApi.toastS(this, context)
}

fun String.toastL(context: Context = GadgetApplication.instance) {
    ToastApi.toastL(this, context)
}

fun String.singleToastS() {
    ToastApi.singleToastS(this)
}

fun String.singleToastL() {
    ToastApi.singleToastL(this)
}