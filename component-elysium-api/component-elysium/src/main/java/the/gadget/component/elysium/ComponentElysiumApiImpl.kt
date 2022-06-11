package the.gadget.component.elysium

import com.google.auto.service.AutoService

@AutoService(ComponentElysiumApi::class)
class ComponentElysiumApiImpl : ComponentElysiumApi {
}

internal val componentElysiumApi: ComponentElysiumApiImpl by lazy { ComponentElysiumApi.instance as ComponentElysiumApiImpl }