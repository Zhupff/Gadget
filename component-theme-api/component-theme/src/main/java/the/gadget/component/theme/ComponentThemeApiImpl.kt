package the.gadget.component.theme

import com.google.auto.service.AutoService
import the.gadget.theme.ThemeApi
import java.util.concurrent.atomic.AtomicBoolean

@AutoService(ComponentThemeApi::class)
class ComponentThemeApiImpl : ComponentThemeApi {
    private val entranceLock: AtomicBoolean = AtomicBoolean(false)

    init {
        ThemeApi.instance.getCurrentScheme().observeForever {
            unlockEntrance()
        }
    }

    internal fun lockEntrance() { entranceLock.set(true) }

    private fun unlockEntrance() { entranceLock.set(false) }

    internal fun isEntranceLock(): Boolean = entranceLock.get()
}

internal val componentThemeApi: ComponentThemeApiImpl by lazy { ComponentThemeApi.instance as ComponentThemeApiImpl }