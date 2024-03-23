package zhupf.gadget.basic

import android.app.Application

lateinit var GADGET: Gadget; private set

class Gadget : Application() {

    init {
        GADGET = this
    }
}