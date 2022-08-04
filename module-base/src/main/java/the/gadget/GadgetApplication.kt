package the.gadget

import android.app.Application

class GadgetApplication : Application() {
    companion object {
        lateinit var instance: GadgetApplication; private set

        val APP_VERSION: String by lazy { instance.packageManager.getPackageInfo(instance.packageName, 0).versionName }
    }

    init { instance = this }
}