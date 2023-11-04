package zhupf.gadget.common

import android.content.Context
import androidx.startup.Initializer

class GadgetCommonInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Toaster.init(context.applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}