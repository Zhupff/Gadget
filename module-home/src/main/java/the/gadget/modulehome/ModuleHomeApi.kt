package the.gadget.modulehome

import android.content.Context
import the.gadget.modulebase.api.apiInstanceOrNull

interface ModuleHomeApi {
    companion object {
        val instance: ModuleHomeApi? by lazy { apiInstanceOrNull(ModuleHomeApi::class.java) }
    }

    fun toHomeActivity(context: Context)
}