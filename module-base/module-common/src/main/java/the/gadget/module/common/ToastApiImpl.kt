package the.gadget.module.common

import android.content.Context
import android.widget.Toast
import com.google.auto.service.AutoService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import the.gadget.api.ApplicationApi
import the.gadget.api.ToastApi

@AutoService(ToastApi::class)
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
            singleToast = Toast.makeText(ApplicationApi.instance.getApplication(), msg, time)
            singleToast?.show()
        }
    }
}