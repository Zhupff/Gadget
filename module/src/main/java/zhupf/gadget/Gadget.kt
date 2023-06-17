package zhupf.gadget

import android.app.Application
import zhupf.gadget.module.BuildConfig
import zhupf.gadget.of.OfLoader

class Gadget : Application() {
    companion object {
        val debugPkg = BuildConfig.DEBUG
        val debugEnv = true
        lateinit var application: Application; private set
    }

    init {
        application = this
    }

    override fun onCreate() {
        super.onCreate()
        OfLoader.DEFAULT_CLASS_LOADER = classLoader
    }
}