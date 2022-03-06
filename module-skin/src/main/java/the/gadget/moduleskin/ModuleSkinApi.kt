package the.gadget.moduleskin

import the.gadget.modulebase.api.apiInstanceOrNull

interface ModuleSkinApi {
    companion object {
        val instance: ModuleSkinApi? by lazy { apiInstanceOrNull(ModuleSkinApi::class.java) }
    }
}