package the.gadget.theme

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

object SkinViewDataBindingAdapter {

    @JvmStatic
    @BindingAdapter("skinBackgroundColor")
    fun skinBackgroundColor(view: View, @ColorRes id: Int) {
        SkinApi.instance.attachView(view).withSkinBackgroundColor(id)
    }

    @JvmStatic
    @BindingAdapter("skinTextColor")
    fun skinTextColor(view: TextView, @ColorRes id: Int) {
        SkinApi.instance.attachView(view).withSkinTextColor(id)
    }

    @JvmStatic
    @BindingAdapter("skinColorFilter")
    fun skinColorFilter(view: ImageView, @ColorRes id: Int) {
        SkinApi.instance.attachView(view).withSkinColorFilter(id)
    }

    @JvmStatic
    @BindingAdapter("skinDrawableRes")
    fun skinDrawableRes(view: ImageView, @DrawableRes id: Int) {
        SkinApi.instance.attachView(view).withSkinDrawableRes(id)
    }
}