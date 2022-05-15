package the.gadget.modulebase.skin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

object SkinViewDataBindingAdapter {

    private val api: SkinApi; get() = SkinApi.instance

    @JvmStatic
    @BindingAdapter("skinBackgroundColor")
    fun skinBackgroundColor(view: View, @ColorRes id: Int) {
        if (id == 0) return
        view.setBackgroundColor(api.getColorInt(id))
    }

    @JvmStatic
    @BindingAdapter("skinTextColor")
    fun skinTextColor(view: TextView, @ColorRes id: Int) {
        if (id == 0) return
        val colorStateList = api.getColorStateList(id)
        if (colorStateList != null) {
            view.setTextColor(colorStateList)
        } else {
            view.setTextColor(api.getColorInt(id))
        }
    }

    @JvmStatic
    @BindingAdapter("skinColorFilter")
    fun skinColorFilter(view: ImageView, @ColorRes id: Int) {
        if (id == 0) return
        view.setColorFilter(api.getColorInt(id))
    }

    @JvmStatic
    @BindingAdapter("skinDrawableRes")
    fun skinDrawableRes(view: ImageView, @DrawableRes id: Int) {
        if (id == 0) return
        view.setImageDrawable(api.getDrawable(id))
    }
}