package gadget.basic.theme

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.logger.Loggable
import gadget.basic.logger.logW
import gadget.basic.theme.ThemeManager.isLightMode

internal object LiteTheme : LiveData<Theme>(), Theme, Loggable by Loggable.Tag(true) {

    override val id: String
        get() = if (isLightMode) "light" else "night"

    init {
        postValue(this)
    }

    override fun getColor(r: Theme.Resource): Int = when (r.id) {
        gadget.basic.R.color.ThemePrimary    -> if (isLightMode) 0xFF445E91.toInt() else 0xFFADC6FF.toInt()
        gadget.basic.R.color.ThemeOnPrimary  -> if (isLightMode) 0xFFFFFFFF.toInt() else 0xFF102F60.toInt()
        gadget.basic.R.color.ThemeBackground -> if (isLightMode) 0xFFFFFFFF.toInt() else 0xFF0C0E13.toInt()
        gadget.basic.R.color.ThemeForeground -> if (isLightMode) 0xFF1A1B20.toInt() else 0xFFE2E2E9.toInt()
        gadget.basic.R.color.ThemeSurface    -> if (isLightMode) 0xFFEDEDF4.toInt() else 0xFF1E1F25.toInt()
        gadget.basic.R.color.ThemeOutline    -> if (isLightMode) 0xFF74777F.toInt() else 0xFF8E9099.toInt()
        gadget.basic.R.color.ThemeError      -> if (isLightMode) 0xFFBA1A1A.toInt() else 0xFFFFB4AB.toInt()
        gadget.basic.R.color.ThemeOnError    -> if (isLightMode) 0xFFFFFFFF.toInt() else 0xFF690005.toInt()
        else -> {
            if (Gadget.debuggable) {
                IllegalStateException("getColor($r) in $id failed!").throws()
            } else {
                logW(null) { "getColor($r) in $id failed!" }
                0
            }
        }
    }

    override fun getDrawable(r: Theme.Resource): Drawable? {
        val color = getColor(r)
        return if (color != 0) color.toDrawable() else null
    }

    override fun getString(r: Theme.Resource, vararg args: Any): String? {
        return null
    }

    public override fun postValue(value: Theme?) {
        super.postValue(value)
    }
}