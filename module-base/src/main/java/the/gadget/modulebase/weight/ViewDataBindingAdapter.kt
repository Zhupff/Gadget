package the.gadget.modulebase.weight

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

object ViewDataBindingAdapter {

    @JvmStatic
    @BindingAdapter("drawableRes")
    fun drawableRes(view: ImageView, @DrawableRes res: Int) {
        view.setImageResource(res)
    }

    @JvmStatic
    @BindingAdapter("visibleOrInvisible")
    fun visibleOrInvisible(view: View, bool: Boolean) {
        view.visibility = if (bool) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("visibleOrGone")
    fun visibleOrGone(view: View, bool: Boolean) {
        view.visibility = if (bool) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("invisibleOrGone")
    fun invisibleOrGone(view: View, bool: Boolean) {
        view.visibility = if (bool) View.INVISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("selected")
    fun selected(view: View, selected: Boolean) {
        view.isSelected = selected
    }
}