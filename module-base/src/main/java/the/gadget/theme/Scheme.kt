package the.gadget.theme

import android.graphics.Bitmap

class Scheme(
    val mode: Mode, val originArgb: Int, val wallpaper: Bitmap?,
    val primary: Int, val onPrimary: Int,
    val primaryContainer: Int, val onPrimaryContainer: Int,
    val secondary: Int, val onSecondary: Int,
    val secondaryContainer: Int, val onSecondaryContainer: Int,
    val tertiary: Int, val onTertiary: Int,
    val tertiaryContainer: Int, val onTertiaryContainer: Int,
    val error: Int, val onError: Int,
    val errorContainer: Int, val onErrorContainer: Int,
    val background: Int, val onBackground: Int,
    val backgroundContainer: Int, val onBackgroundContainer: Int,
    val surface: Int, val outline: Int, val shadow: Int,
    val inversePrimary: Int, val inverseSecondary: Int, val inverseTertiary: Int,
) {
    enum class Mode {
        Light, Dark,
        ;
        fun isLightMode(): Boolean = this == Light
        fun isDarkMode(): Boolean = this == Dark
        fun reverse(): Mode = if (isLightMode()) Dark else Light
    }

    fun getColour(colour: Colour): Int = when (colour) {
        Colour.Origin                -> originArgb
        Colour.Primary               -> primary
        Colour.OnPrimary             -> onPrimary
        Colour.PrimaryContainer      -> primaryContainer
        Colour.OnPrimaryContainer    -> onPrimaryContainer
        Colour.Secondary             -> secondary
        Colour.OnSecondary           -> onSecondary
        Colour.SecondaryContainer    -> secondaryContainer
        Colour.OnSecondaryContainer  -> onSecondaryContainer
        Colour.Tertiary              -> tertiary
        Colour.OnTertiary            -> onTertiary
        Colour.TertiaryContainer     -> tertiaryContainer
        Colour.OnTertiaryContainer   -> onTertiaryContainer
        Colour.Error                 -> error
        Colour.OnError               -> onError
        Colour.ErrorContainer        -> errorContainer
        Colour.OnErrorContainer      -> onErrorContainer
        Colour.Background            -> background
        Colour.OnBackground          -> onBackground
        Colour.BackgroundContainer   -> backgroundContainer
        Colour.OnBackgroundContainer -> onBackgroundContainer
        Colour.Surface               -> surface
        Colour.Outline               -> outline
        Colour.Shadow                -> shadow
        Colour.InversePrimary        -> inversePrimary
        Colour.InverseSecondary      -> inverseSecondary
        Colour.InverseTertiary       -> inverseTertiary
    }

    override fun hashCode(): Int = originArgb

    override fun equals(other: Any?): Boolean =
        other is Scheme && other.mode == this.mode && other.originArgb == this.originArgb && this.wallpaper === other.wallpaper
}