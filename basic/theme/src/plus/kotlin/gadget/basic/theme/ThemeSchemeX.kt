package gadget.basic.theme

import android.annotation.SuppressLint
import com.google.android.material.color.utilities.Hct
import com.google.android.material.color.utilities.SchemeTonalSpot
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.theme.ThemeScheme.Light
import gadget.basic.theme.ThemeScheme.Night

@SuppressLint("RestrictedApi")
fun <T : Light> T.create(seed: Int, night: Night? = null): Light {
    if (seed == 0) {
        if (Gadget.debuggable) {
            IllegalArgumentException("seed should not be 0!").throws()
        } else {
            return Light
        }
    }
    val scheme = SchemeTonalSpot(Hct.fromInt(seed), false, 0.0)
    val light = object : Light() {
        override val primaryColor   : Int = scheme.primary
        override val onPrimaryColor : Int = scheme.onPrimary
        override val backgroundColor: Int = scheme.surfaceContainerLowest
        override val foregroundColor: Int = scheme.onSurface
        override val surfaceColor   : Int = scheme.surfaceContainer
        override val outlineColor   : Int = scheme.outline
        override val errorColor     : Int = scheme.error
        override val onErrorColor   : Int = scheme.onError
    }
    light.night = night ?: this.night.create(seed, light)
    return light
}

@SuppressLint("RestrictedApi")
fun <T : Night> T.create(seed: Int, light: Light? = null): Night {
    if (seed == 0) {
        if (Gadget.debuggable) {
            IllegalArgumentException("seed should not be 0!").throws()
        } else {
            return Night
        }
    }
    val scheme = SchemeTonalSpot(Hct.fromInt(seed), true, 0.0)
    val night = object : Night() {
        override val primaryColor   : Int = scheme.primary
        override val onPrimaryColor : Int = scheme.onPrimary
        override val backgroundColor: Int = scheme.surfaceContainerLowest
        override val foregroundColor: Int = scheme.onSurface
        override val surfaceColor   : Int = scheme.surfaceContainer
        override val outlineColor   : Int = scheme.outline
        override val errorColor     : Int = scheme.error
        override val onErrorColor   : Int = scheme.onError
    }
    night.light = light ?: this.light.create(seed, night)
    return night
}
