package the.gadget.theme

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import the.gadget.module.base.R

abstract class ThemeView(val view: View) : Observer<Palette> {

    companion object {
        fun get(view: View): ThemeView? = view.getTag(R.id.theme_view_tag) as? ThemeView

        @JvmStatic
        @BindingAdapter("themeBackgroundColor")
        fun themeBackgroundColor(view: View, colour: Colour) {
            ThemeApi.instance.attachView(view).backgroundColor(colour)
        }

        @JvmStatic
        @BindingAdapter("themeTextColor")
        fun themeTextColor(view: View, colour: Colour) {
            ThemeApi.instance.attachView(view).textColor(colour)
        }

        @JvmStatic
        @BindingAdapter("themeColorFilter")
        fun themeColorFilter(view: View, colour: Colour) {
            ThemeApi.instance.attachView(view).colorFilter(colour)
        }
    }

    abstract fun release()

    abstract fun backgroundColor(colour: Colour): ThemeView

    abstract fun textColor(colour: Colour): ThemeView

    abstract fun colorFilter(colour: Colour): ThemeView
}