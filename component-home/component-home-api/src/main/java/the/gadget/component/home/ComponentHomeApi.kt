package the.gadget.component.home

import android.content.Context
import the.gadget.api.NullableApi
import the.gadget.api.globalApi

interface ComponentHomeApi : NullableApi {
    companion object {
        val instance: ComponentHomeApi? by lazy { ComponentHomeApi::class.globalApi() }
    }

    fun toHomeActivity(context: Context)
}