package the.gadget.moduleskin

import java.util.*

interface ModuleSkinApi {
    companion object {
        val instance: ModuleSkinApi? by lazy {
            ServiceLoader.load(ModuleSkinApi::class.java).firstOrNull()
        }
    }
}