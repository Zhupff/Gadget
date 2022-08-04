package the.gadget.component.setting

import the.gadget.api.NullableApi
import the.gadget.api.globalApi

interface ComponentSettingApi : NullableApi {
    companion object {
        val instance: ComponentSettingApi? by lazy { ComponentSettingApi::class.globalApi() }
    }
}