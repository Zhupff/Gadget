package the.gadget.modulebase.skin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter

object SkinViewDataBindingAdapter {

    private val api: SkinApi; get() = SkinApi.instance

    @JvmStatic
    @BindingAdapter("srBackgroundColor")
    fun srBackgroundColor(view: View, @ColorRes id: Int) {
        view.setBackgroundColor(api.getColorInt(id))
    }

    @JvmStatic
    @BindingAdapter("srTextColor")
    fun srTextColor(view: TextView, @ColorRes id: Int) {
        val colorStateList = api.getColorStateList(id)
        if (colorStateList != null) {
            view.setTextColor(colorStateList)
        } else {
            view.setTextColor(api.getColorInt(id))
        }
    }

    @JvmStatic
    @BindingAdapter("srColorFilter")
    fun srColorFilter(view: ImageView, @ColorRes id: Int) {
        view.setColorFilter(api.getColorInt(id))
    }
}