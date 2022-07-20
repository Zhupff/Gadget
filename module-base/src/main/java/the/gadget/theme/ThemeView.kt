package the.gadget.theme

import android.view.View

abstract class ThemeView(val view: View) {

    abstract fun release()

    abstract fun backgroundColor(colour: Colour): ThemeView

    abstract fun textColor(colour: Colour): ThemeView

    abstract fun hintColor(colour: Colour): ThemeView

    abstract fun foregroundTint(colour: Colour): ThemeView

    abstract fun backgroundTint(colour: Colour): ThemeView

    abstract fun wallpaper(): ThemeView
}