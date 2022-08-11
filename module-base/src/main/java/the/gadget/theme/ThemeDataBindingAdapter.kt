@file:JvmName("ThemeDataBindingAdapter")

package the.gadget.theme

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("themeBackgroundColor")
fun themeBackgroundColor(view: View, colour: Colour) {
    ThemeApi.attachView(view).backgroundColor(colour)
}

@BindingAdapter("themeTextColor")
fun themeTextColor(view: View, colour: Colour) {
    ThemeApi.attachView(view).textColor(colour)
}

@BindingAdapter("themeHintColor")
fun themeHintColor(view: View, colour: Colour) {
    ThemeApi.attachView(view).hintColor(colour)
}

@BindingAdapter("themeForegroundTint")
fun themeForegroundTint(view: View, colour: Colour) {
    ThemeApi.attachView(view).foregroundTint(colour)
}

@BindingAdapter("themeBackgroundTint")
fun themeBackgroundTint(view: View, colour: Colour) {
    ThemeApi.attachView(view).backgroundTint(colour)
}