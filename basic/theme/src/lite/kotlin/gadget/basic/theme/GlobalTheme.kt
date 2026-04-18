package gadget.basic.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gadget.basic.Gadget
import gadget.basic.tool.mutable

object GlobalTheme {

    val current: LiveData<ThemeScheme> = MutableLiveData()

    init {
        Gadget.configuration.observeForever { configuration ->
            val oldScheme = current.value
            val newScheme = if (configuration.isNightModeActive) Night else Light
            if (oldScheme == null) {
                current.mutable().value = newScheme
            } else if (newScheme !== oldScheme) {
                current.mutable().postValue(newScheme)
            }
        }
    }

    private object Light : ThemeScheme {
        override val primaryColor   : Int = 0xFF445E91.toInt()
        override val onPrimaryColor : Int = 0xFFFFFFFF.toInt()
        override val backgroundColor: Int = 0xFFFFFFFF.toInt()
        override val foregroundColor: Int = 0xFF1A1B20.toInt()
        override val surfaceColor   : Int = 0xFFEDEDF4.toInt()
        override val outlineColor   : Int = 0xFF74777F.toInt()
        override val errorColor     : Int = 0xFFBA1A1A.toInt()
        override val onErrorColor   : Int = 0xFFFFFFFF.toInt()
    }

    private object Night : ThemeScheme {
        override val primaryColor   : Int = 0xFFADC6FF.toInt()
        override val onPrimaryColor : Int = 0xFF102F60.toInt()
        override val backgroundColor: Int = 0xFF0C0E13.toInt()
        override val foregroundColor: Int = 0xFFE2E2E9.toInt()
        override val surfaceColor   : Int = 0xFF1E1F25.toInt()
        override val outlineColor   : Int = 0xFF8E9099.toInt()
        override val errorColor     : Int = 0xFFFFB4AB.toInt()
        override val onErrorColor   : Int = 0xFF690005.toInt()
    }
}