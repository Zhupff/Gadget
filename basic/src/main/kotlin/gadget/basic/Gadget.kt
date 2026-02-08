package gadget.basic

import android.app.Application

open class Gadget : Application() {

    companion object {
        lateinit var application: Gadget
            private set
    }

    init {
        Gadget.application = this
    }
}