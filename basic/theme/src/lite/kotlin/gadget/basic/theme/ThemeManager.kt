package gadget.basic.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import gadget.basic.Gadget

object ThemeManager : AbstractThemeManager(), Observer<Gadget.AppLifecycle.State> {

    @Volatile var isLightMode: Boolean = !Gadget.application.resources.configuration.isNightModeActive
        private set(value) {
            if (field != value) {
                field = value
                LiteTheme.postValue(LiteTheme)
            }
        }

    init {
        Gadget.AppLifecycle.observeForever(this)
    }

    override fun subscribe(): LiveData<out Theme> = LiteTheme

    override fun onChanged(value: Gadget.AppLifecycle.State) {
        if (value is Gadget.AppLifecycle.State.OnAppConfigurationChanged) {
            isLightMode = !value.newConfiguration.isNightModeActive
        }
    }
}