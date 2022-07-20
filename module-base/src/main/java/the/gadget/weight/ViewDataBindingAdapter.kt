@file:JvmName("ViewDataBindingAdapter")

package the.gadget.weight

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("foregroundDrawableRes")
fun foregroundDrawableRes(view: ImageView, @DrawableRes id: Int) {
    view.setImageResource(id)
}

@BindingAdapter("foregroundBitmap")
fun foregroundBitmap(view: ImageView, bitmap: Bitmap?) {
    view.setImageBitmap(bitmap)
}

@BindingAdapter("visibleOrInvisible")
fun visibleOrInvisible(view: View, bool: Boolean) {
    view.beVisibleOrInvisible(bool)
}

@BindingAdapter("visibleOrGone")
fun visibleOrGone(view: View, bool: Boolean) {
    view.beVisibleOrGone(bool)
}

@BindingAdapter("invisibleOrGone")
fun invisibleOrGone(view: View, bool: Boolean) {
    view.beInvisibleOrGone(bool)
}

@BindingAdapter("isSelected")
fun isSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}