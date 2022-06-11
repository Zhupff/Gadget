package the.gadget.component.home

import android.content.Context
import the.gadget.api.apiInstanceOrNull

interface ComponentHomeApi {
    companion object {
        val instance: ComponentHomeApi? by lazy { apiInstanceOrNull(ComponentHomeApi::class.java) }
    }

    fun toHomeActivity(context: Context)
}