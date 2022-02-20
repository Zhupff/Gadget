package the.gadget.modulehome

import java.util.*

interface ModuleHomeApi {
    companion object {
        val instance: ModuleHomeApi? by lazy {
            ServiceLoader.load(ModuleHomeApi::class.java).firstOrNull()
        }
    }
}