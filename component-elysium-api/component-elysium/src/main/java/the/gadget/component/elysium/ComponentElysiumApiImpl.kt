package the.gadget.component.elysium

import the.gadget.api.GlobalApi

@GlobalApi(ComponentElysiumApi::class)
class ComponentElysiumApiImpl : ComponentElysiumApi {
}

internal val componentElysiumApi: ComponentElysiumApiImpl by lazy { ComponentElysiumApi.instance as ComponentElysiumApiImpl }