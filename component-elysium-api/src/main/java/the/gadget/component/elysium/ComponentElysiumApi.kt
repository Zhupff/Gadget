package the.gadget.component.elysium

import the.gadget.api.NullableApi
import the.gadget.api.globalApi

interface ComponentElysiumApi : NullableApi {
    companion object {
        val instance: ComponentElysiumApi? by lazy { ComponentElysiumApi::class.globalApi() }
    }
}