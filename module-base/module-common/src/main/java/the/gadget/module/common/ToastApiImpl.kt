package the.gadget.module.common

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import the.gadget.GadgetApplication
import the.gadget.api.GlobalApi
import the.gadget.common.ToastApi

@GlobalApi(ToastApi::class)
class ToastApiImpl : ToastApi {

    private var singleToast: Toast? = null

    override fun toastS(msg: String, context: Context) { toast(msg, context, Toast.LENGTH_SHORT) }

    override fun toastL(msg: String, context: Context) { toast(msg, context, Toast.LENGTH_LONG) }

    override fun singleToastS(msg: String) { singleToast(msg, Toast.LENGTH_SHORT) }

    override fun singleToastL(msg: String) { singleToast(msg, Toast.LENGTH_LONG) }

    private fun toast(msg: String, context: Context, time: Int) {
        MainScope().launch {
            Toast.makeText(context, msg, time).show()
        }
    }

    private fun singleToast(msg: String, time: Int) {
        MainScope().launch {
            singleToast?.cancel()
            singleToast = Toast.makeText(GadgetApplication.instance, msg, time)
            singleToast?.show()
        }
    }
}