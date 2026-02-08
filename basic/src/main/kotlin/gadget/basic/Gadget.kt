package gadget.basic

import android.app.Application
import android.content.pm.ApplicationInfo

open class Gadget : Application() {

    companion object {
        lateinit var application: Gadget
            private set

        val debuggable: Boolean by lazy {
            (application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }
    }

    init {
        Gadget.application = this
    }
}