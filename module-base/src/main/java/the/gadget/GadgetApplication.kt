package the.gadget

import android.app.Application
import the.gadget.api.ApplicationApi

class GadgetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationApi.instance.setApplication(this)
    }
}