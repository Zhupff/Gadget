package the.gadget.weight

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter

object ViewDataBindingAdapter {

    @JvmStatic
    @BindingAdapter("foregroundDrawableRes")
    fun foregroundDrawableRes(view: ImageView, @DrawableRes id: Int) {
        if (id == ResourcesCompat.ID_NULL) return
        view.setImageResource(id)
    }

    @JvmStatic
    @BindingAdapter("foregroundBitmap")
    fun foregroundBitmap(view: ImageView, bitmap: Bitmap) {
        view.setImageBitmap(bitmap)
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
    @BindingAdapter("isSelected")
    fun isSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }
}