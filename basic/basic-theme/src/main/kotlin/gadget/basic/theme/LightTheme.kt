package gadget.basic.theme

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.log.Loggable
import gadget.basic.log.logW

abstract class LightTheme(
    val name: String,
) : Theme, Loggable by Loggable.Tag(true) {

    companion object : LightTheme("Default")

    override val id: String = "${name}-light"

    override fun getColor(r: Theme.Resource): Int = when (r.id) {
        gadget.basic.R.color.ThemePrimary    -> 0xFF445E91.toInt()
        gadget.basic.R.color.ThemeOnPrimary  -> 0xFFFFFFFF.toInt()
        gadget.basic.R.color.ThemeBackground -> 0xFFFFFFFF.toInt()
        gadget.basic.R.color.ThemeForeground -> 0xFF1A1B20.toInt()
        gadget.basic.R.color.ThemeSurface    -> 0xFFEDEDF4.toInt()
        gadget.basic.R.color.ThemeOutline    -> 0xFF74777F.toInt()
        gadget.basic.R.color.ThemeError      -> 0xFFBA1A1A.toInt()
        gadget.basic.R.color.ThemeOnError    -> 0xFFFFFFFF.toInt()
        else -> {
            if (Gadget.debuggable) {
                IllegalStateException("getColor($r) failed!").throws()
            } else {
                logW(null) { "getColor($r) failed!" }
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
}