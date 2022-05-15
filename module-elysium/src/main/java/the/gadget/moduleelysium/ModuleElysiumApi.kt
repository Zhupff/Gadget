package the.gadget.moduleelysium

import the.gadget.modulebase.api.apiInstanceOrNull

interface ModuleElysiumApi {
    companion object {
        val instance: ModuleElysiumApi? by lazy { apiInstanceOrNull(ModuleElysiumApi::class.java) }
    }
}