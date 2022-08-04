package the.gadget.component.setting

import the.gadget.api.GlobalApi

@GlobalApi(ComponentSettingApi::class)
class ComponentSettingApiImpl : ComponentSettingApi {
}

internal val componentSettingApi: ComponentSettingApiImpl by lazy { ComponentSettingApi.instance as ComponentSettingApiImpl }