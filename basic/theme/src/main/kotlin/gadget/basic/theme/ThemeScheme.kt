package gadget.basic.theme

import android.view.View
import androidx.lifecycle.LiveData

interface ThemeScheme {
    
    val primaryColor: Int
    
    val onPrimaryColor: Int
    
    val backgroundColor: Int

    val foregroundColor: Int
    
    val surfaceColor: Int
    
    val outlineColor: Int
    
    val errorColor: Int
    
    val onErrorColor: Int

    open class Light : ThemeScheme {
        companion object : Light()
        override val primaryColor   : Int = 0xFF445E91.toInt()
        override val onPrimaryColor : Int = 0xFFFFFFFF.toInt()
        override val backgroundColor: Int = 0xFFFFFFFF.toInt()
        override val foregroundColor: Int = 0xFF1A1B20.toInt()
        override val surfaceColor   : Int = 0xFFEDEDF4.toInt()
        override val outlineColor   : Int = 0xFF74777F.toInt()
        override val errorColor     : Int = 0xFFBA1A1A.toInt()
        override val onErrorColor   : Int = 0xFFFFFFFF.toInt()
        internal lateinit var night: Night
    }

    open class Night : ThemeScheme {
        companion object : Night()
        override val primaryColor   : Int = 0xFFADC6FF.toInt()
        override val onPrimaryColor : Int = 0xFF102F60.toInt()
        override val backgroundColor: Int = 0xFF0C0E13.toInt()
        override val foregroundColor: Int = 0xFFE2E2E9.toInt()
        override val surfaceColor   : Int = 0xFF1E1F25.toInt()
        override val outlineColor   : Int = 0xFF8E9099.toInt()
        override val errorColor     : Int = 0xFFFFB4AB.toInt()
        override val onErrorColor   : Int = 0xFF690005.toInt()
        internal lateinit var light: Light
    }
}

fun View.theme(
    lambda: ((ThemeScheme).() -> Unit)?,
) {
    ThemeSubscriber.get(this).subscribe(action = lambda ?: ThemeSubscriber.NO_ACTION)
}

fun View.subscribeTheme(
    observable: LiveData<out ThemeScheme>?,
    lambda: ((ThemeScheme).() -> Unit)? = null,
) {
    ThemeSubscriber.get(this).subscribe(observable, lambda ?: ThemeSubscriber.NO_ACTION)
}
