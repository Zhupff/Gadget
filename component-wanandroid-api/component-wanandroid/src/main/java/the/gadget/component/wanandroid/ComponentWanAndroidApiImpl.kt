package the.gadget.component.wanandroid

import the.gadget.api.GlobalApi

@GlobalApi(ComponentWanAndroidApi::class)
class ComponentWanAndroidApiImpl : ComponentWanAndroidApi {
}

internal val componentWanAndroidApi: ComponentWanAndroidApiImpl by lazy { ComponentWanAndroidApi.instance as ComponentWanAndroidApiImpl }