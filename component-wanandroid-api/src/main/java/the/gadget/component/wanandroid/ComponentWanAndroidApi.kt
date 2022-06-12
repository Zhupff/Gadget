package the.gadget.component.wanandroid

import the.gadget.api.apiInstanceOrNull

interface ComponentWanAndroidApi {
    companion object {
        val instance: ComponentWanAndroidApi? by lazy { apiInstanceOrNull(ComponentWanAndroidApi::class.java) }
    }
}