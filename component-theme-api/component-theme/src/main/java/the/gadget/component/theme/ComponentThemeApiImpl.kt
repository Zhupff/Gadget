package the.gadget.component.theme

import com.google.auto.service.AutoService

@AutoService(ComponentThemeApi::class)
class ComponentThemeApiImpl : ComponentThemeApi {
}

internal val componentThemeApi: ComponentThemeApiImpl by lazy { ComponentThemeApi.instance as ComponentThemeApiImpl }