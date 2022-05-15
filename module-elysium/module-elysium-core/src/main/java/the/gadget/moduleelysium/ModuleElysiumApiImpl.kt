package the.gadget.moduleelysium

import com.google.auto.service.AutoService

@AutoService(ModuleElysiumApi::class)
class ModuleElysiumApiImpl : ModuleElysiumApi {
}

internal val moduleElysiumApi: ModuleElysiumApiImpl by lazy { ModuleElysiumApi.instance as ModuleElysiumApiImpl }