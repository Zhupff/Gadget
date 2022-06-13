package the.gadget.component.theme

import the.gadget.api.apiInstanceOrNull

interface ComponentThemeApi {
    companion object {
        val instance: ComponentThemeApi? by lazy { apiInstanceOrNull(ComponentThemeApi::class.java) }
    }
}