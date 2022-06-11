package the.gadget.component.elysium

import the.gadget.api.apiInstanceOrNull

interface ComponentElysiumApi {
    companion object {
        val instance: ComponentElysiumApi? by lazy { apiInstanceOrNull(ComponentElysiumApi::class.java) }
    }
}