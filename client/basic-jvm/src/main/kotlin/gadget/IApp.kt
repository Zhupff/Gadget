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

    interface Task {
        val priority: Int
        suspend fun execute()
    }
}