package the.gadget.modulebase.common

import android.widget.Toast
import com.google.auto.service.AutoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.modulebase.application.ApplicationApi

@AutoService(ToastApi::class)
class ToastApiImpl : ToastApi {
    override fun toast(msg: String) {
        toast(msg, Toast.LENGTH_SHORT)
    }

    override fun longToast(msg: String) {
        toast(msg, Toast.LENGTH_LONG)
    }

    private fun toast(msg: String, time: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(ApplicationApi.instance.getApplication(), msg, time).show()
        }
    }
}