package the.gadget.modulebase.skin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter

object SkinViewDataBindingAdapter {

    private val api: SkinApi; get() = SkinApi.instance

    @JvmStatic
    @BindingAdapter("skinBackgroundColor")
    fun skinBackgroundColor(view: View, @ColorRes id: Int) {
        view.setBackgroundColor(api.getColorInt(id))
    }

    @JvmStatic
    @BindingAdapter("skinTextColor")
    fun skinTextColor(view: TextView, @ColorRes id: Int) {
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
        view.setColorFilter(api.getColorInt(id))
    }
}