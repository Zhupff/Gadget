package gadget.basic.theme

import androidx.lifecycle.MutableLiveData

object ThemeManager {

    object Light : Theme {
        override val id: String = "light"
        override val primaryColor: Int = 0xFF445E91.toInt()
        override val onPrimaryColor: Int = 0xFFFFFFFF.toInt()
        override val backgroundColor: Int = 0xFFFFFFFF.toInt()
        override val foregroundColor: Int = 0xFF1A1B20.toInt()
        override val surfaceColor: Int = 0xFFEDEDF4.toInt()
        override val outlineColor: Int = 0xFF74777F.toInt()
        override val errorColor: Int = 0xFFBA1A1A.toInt()
        override val onErrorColor: Int = 0xFFFFFFFF.toInt()
    }

    object Night : Theme {
        override val id: String = "night"
        override val primaryColor: Int = 0xFFADC6FF.toInt()
        override val onPrimaryColor: Int = 0xFF102F60.toInt()
        override val backgroundColor: Int = 0xFF0C0E13.toInt()
        override val foregroundColor: Int = 0xFFE2E2E9.toInt()
        override val surfaceColor: Int = 0xFF1E1F25.toInt()
        override val outlineColor: Int = 0xFF8E9099.toInt()
        override val errorColor: Int = 0xFFFFB4AB.toInt()
        override val onErrorColor: Int = 0xFF690005.toInt()
    }

    val observable = MutableLiveData<Theme>(Light)

    fun switch() {
        if (observable.value === Light) {
            observable.value = Night
        } else {
            observable.value = Light
        }
    }
}