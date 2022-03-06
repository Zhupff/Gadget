package the.gadget.modulehome

import the.gadget.modulebase.api.apiInstanceOrNull

interface ModuleHomeApi {
    companion object {
        val instance: ModuleHomeApi? by lazy { apiInstanceOrNull(ModuleHomeApi::class.java) }
    }
}