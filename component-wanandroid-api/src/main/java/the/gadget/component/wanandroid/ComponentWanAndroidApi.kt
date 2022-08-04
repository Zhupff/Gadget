package the.gadget.component.wanandroid

import the.gadget.api.NullableApi
import the.gadget.api.globalApi

interface ComponentWanAndroidApi : NullableApi {
    companion object {
        val instance: ComponentWanAndroidApi? by lazy { ComponentWanAndroidApi::class.globalApi() }
    }
}