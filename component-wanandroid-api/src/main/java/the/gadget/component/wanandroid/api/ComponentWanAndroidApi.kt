package the.gadget.component.wanandroid.api

import the.gadget.api.apiInstanceOrNull

interface ComponentWanAndroidApi {
    companion object {
        val instance: ComponentWanAndroidApi? by lazy { apiInstanceOrNull(ComponentWanAndroidApi::class.java) }
    }
}