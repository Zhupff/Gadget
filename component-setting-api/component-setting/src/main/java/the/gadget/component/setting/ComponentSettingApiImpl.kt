package the.gadget.component.setting

import com.google.auto.service.AutoService

@AutoService(ComponentSettingApi::class)
class ComponentSettingApiImpl : ComponentSettingApi {
}

internal val componentSettingApi: ComponentSettingApiImpl by lazy { ComponentSettingApi.instance as ComponentSettingApiImpl }