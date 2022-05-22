package the.gadget.moduleskin

import android.content.Context
import the.gadget.modulebase.api.apiInstanceOrNull

interface ModuleSkinApi {
    companion object {
        val instance: ModuleSkinApi? by lazy { apiInstanceOrNull(ModuleSkinApi::class.java) }
    }

    fun toSkinActivity(context: Context)
}