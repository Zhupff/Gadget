package the.gadget.component.wanandroid

import com.google.auto.service.AutoService

@AutoService(ComponentWanAndroidApi::class)
class ComponentWanAndroidApiImpl : ComponentWanAndroidApi {
}

internal val componentWanAndroidApi: ComponentWanAndroidApiImpl by lazy { ComponentWanAndroidApi.instance as ComponentWanAndroidApiImpl }