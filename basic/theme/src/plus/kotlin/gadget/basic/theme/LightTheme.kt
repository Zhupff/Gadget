package gadget.basic.theme

import com.google.mcu.hct.Hct
import com.google.mcu.scheme.SchemeTonalSpot
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.logger.logW

internal open class LightTheme(
    name: String,
    parent: LightTheme? = null,
) : AbstractTheme<LightTheme>(name, parent) {

    companion object : LightTheme("Default") {
        override var night: NightTheme?
            set(_) = IllegalArgumentException("Can not set night-theme to default light-theme!").throws()
            get() = NightTheme
    }

    final override val id: String = "${name}-Light"

    override var primaryColor   : Int = 0xFF445E91.toInt()
        protected set
    override var onPrimaryColor : Int = 0xFFFFFFFF.toInt()
        protected set
    override var backgroundColor: Int = 0xFFFFFFFF.toInt()
        protected set
    override var foregroundColor: Int = 0xFF1A1B20.toInt()
        protected set
    override var surfaceColor   : Int = 0xFFEDEDF4.toInt()
        protected set
    override var outlineColor   : Int = 0xFF74777F.toInt()
        protected set
    override var errorColor     : Int = 0xFFBA1A1A.toInt()
        protected set
    override var onErrorColor   : Int = 0xFFFFFFFF.toInt()
        protected set

    open var night: NightTheme? = null
        protected set

    fun create(name: String, argb: Int, night: NightTheme?): LightTheme {
        if (name == this.name) {
            if (Gadget.debuggable) {
                IllegalArgumentException("The theme with the same name is already exists!").throws()
            } else {
                logW(null) { "The theme with the same name is already exists!" }
                return LightTheme
            }
        }
        if (argb == 0) {
            if (Gadget.debuggable) {
                IllegalArgumentException("argb should not be 0!").throws()
            } else {
                logW(null) { "argb should not be 0!" }
                return LightTheme
            }
        }
        val scheme = SchemeTonalSpot(Hct.fromInt(argb), false, 0.0)
        val theme = LightTheme(name, this).also { theme ->
            theme.primaryColor = scheme.primary
            theme.onPrimaryColor = scheme.onPrimary
            theme.backgroundColor = scheme.surfaceContainerLowest
            theme.foregroundColor = scheme.onSurface
            theme.surfaceColor = scheme.surfaceContainer
            theme.outlineColor = scheme.outline
            theme.errorColor = scheme.error
            theme.onErrorColor = scheme.onError
        }
        if (night != null) {
            theme.night = night
        } else {
            theme.night = this.night?.create(name, argb, theme)
        }
        return theme
    }
}