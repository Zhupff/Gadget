package the.gadget.component.skin

import android.content.Context
import the.gadget.api.apiInstanceOrNull

interface ComponentSkinApi {
    companion object {
        val instance: ComponentSkinApi? by lazy { apiInstanceOrNull(ComponentSkinApi::class.java) }
    }

    fun toSkinActivity(context: Context)
}