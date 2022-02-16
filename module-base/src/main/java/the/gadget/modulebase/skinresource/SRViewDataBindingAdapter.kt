package the.gadget.modulebase.skinresource

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter

object SRViewDataBindingAdapter {

    private val api: SRApi; get() = SRApi.instance

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