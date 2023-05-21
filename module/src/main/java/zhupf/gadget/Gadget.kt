package zhupf.gadget

import android.app.Application
import zhupf.gadget.module.BuildConfig

class Gadget : Application() {
    companion object {
        val debugPkg = BuildConfig.DEBUG
        val debugEnv = true
        lateinit var application: Application; private set
    }

    init {
        application = this
    }
}