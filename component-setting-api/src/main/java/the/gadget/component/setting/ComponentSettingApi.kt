package the.gadget.component.setting

import the.gadget.api.apiInstanceOrNull

interface ComponentSettingApi {
    companion object {
        val instance: ComponentSettingApi? by lazy { apiInstanceOrNull(ComponentSettingApi::class.java) }
    }
}