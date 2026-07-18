package gadget.basic

import android.app.Application

abstract class GadgetApplication : Application() {

    companion object {
        lateinit var alyx: GadgetApplication
            private set
    }

    init {
        alyx = this
    }
}