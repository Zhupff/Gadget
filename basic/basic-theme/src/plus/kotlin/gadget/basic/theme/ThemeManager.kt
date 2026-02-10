package gadget.basic.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import gadget.basic.Gadget

object ThemeManager : AbstractThemeManager(), Observer<Gadget.AppLifecycle.State> {

    private val observable = MutableLiveData<Theme>()

    init {
        val isLightMode = !Gadget.application.resources.configuration.isNightModeActive
        if (isLightMode) {
            observable.postValue(LightTheme)
        } else {
            observable.postValue(NightTheme)
        }
    }

    override fun subscribe(): LiveData<out Theme> = observable

    override fun onChanged(value: Gadget.AppLifecycle.State) {
        if (value is Gadget.AppLifecycle.State.OnAppConfigurationChanged) {
            val isLightMode = !value.newConfiguration.isNightModeActive
            val currentTheme = observable.value
            if (isLightMode && currentTheme is NightTheme) {
                currentTheme.light?.let(observable::postValue)
            } else if (!isLightMode && currentTheme is LightTheme) {
                currentTheme.night?.let(observable::postValue)
            }
        }
    }
}