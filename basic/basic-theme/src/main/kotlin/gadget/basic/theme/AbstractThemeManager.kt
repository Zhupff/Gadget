package gadget.basic.theme

import androidx.lifecycle.Lifecycle
import gadget.basic.Gadget

abstract class AbstractThemeManager internal constructor() : ThemeObservable {

    final override val lifecycle: Lifecycle
        get() = Gadget.AppLifecycle.lifecycle
}