package gadget.basic.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gadget.basic.Gadget
import gadget.basic.tool.mutable

object ThemeManager {

    init {
        val light = ThemeScheme.Light
        val night = ThemeScheme.Night
        light.night = night
        night.light = light
    }

    val observable: LiveData<ThemeScheme> = MutableLiveData(if (Gadget.configuration.value?.isNightModeActive == true) ThemeScheme.Night else ThemeScheme.Light)

    init {
        Gadget.configuration.observeForever { switch(observable.value ?: return@observeForever) }
    }

    fun switch(scheme: ThemeScheme) {
        val isNightMode = Gadget.configuration.value?.isNightModeActive == true
        val target = if (isNightMode) {
            if (scheme is ThemeScheme.Light) {
                scheme.night
            } else {
                scheme
            }
        } else {
            if (scheme is ThemeScheme.Night) {
                scheme.light
            } else {
                scheme
            }
        }
        if (observable.value !== target) {
            observable.mutable().postValue(target)
        }
    }
}