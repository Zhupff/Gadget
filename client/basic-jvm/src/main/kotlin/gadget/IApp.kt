package gadget

import java.io.File

interface IApp {

    companion object {
        private lateinit var instance: IApp
    }

    val debuggable: Boolean

    val configDir: File

    fun init() {
        instance = this
    }

    interface Startup : Runnable {
        val priority: Int
        suspend fun post() {}
        override fun run() {}

        interface MainStartup : Startup
    }
}