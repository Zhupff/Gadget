package gadget.basic.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import gadget.basic.Gadget

object ThemeManager : AbstractThemeManager(), Observer<Gadget.AppLifecycle.State> {

    @Volatile var isLightMode: Boolean = !Gadget.application.resources.configuration.isNightModeActive
        private set(value) {
            if (field != value) {
                field = value
            }
        }

    private val observable = MutableLiveData<Theme>()

    init {
        if (isLightMode) {
            observable.postValue(LightTheme)
        } else {
            observable.postValue(NightTheme)
        }
        Gadget.AppLifecycle.observeForever(this)
    }

    override fun subscribe(): LiveData<out Theme> = observable

    override fun onChanged(value: Gadget.AppLifecycle.State) {
        if (value is Gadget.AppLifecycle.State.OnAppConfigurationChanged) {
            isLightMode = !value.newConfiguration.isNightModeActive
            val currentTheme = observable.value
            if (isLightMode && currentTheme is NightTheme) {
                currentTheme.light?.let(observable::postValue)
            } else if (!isLightMode && currentTheme is LightTheme) {
                currentTheme.night?.let(observable::postValue)
            }
        }
    }

    fun switch(newTheme: Theme) {
        val currentTheme = observable.value
        if (newTheme.id == currentTheme?.id) {
            return
        }
        if (isLightMode) {
            if (newTheme is LightTheme) {
                observable.postValue(newTheme)
                return
            } else if (newTheme is NightTheme) {
                observable.postValue(newTheme.light ?: newTheme)
                return
            }
        } else {
            if (newTheme is NightTheme) {
                observable.postValue(newTheme)
                return
            } else if (newTheme is LightTheme) {
                observable.postValue(newTheme.night ?: newTheme)
                return
            }
        }
        observable.postValue(newTheme)
    }
}