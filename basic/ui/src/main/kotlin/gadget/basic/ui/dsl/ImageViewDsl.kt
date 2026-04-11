package gadget.basic.ui.dsl

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import gadget.basic.annotation.DslScope

//private typealias IMAGE_VIEW = ImageView
private typealias IMAGE_VIEW = AppCompatImageView


inline fun <V : ViewGroup> ViewScope<V>.ImageView(
    params: (@DslScope IMAGE_VIEW).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<IMAGE_VIEW>).() -> Unit = {},
): IMAGE_VIEW = IMAGE_VIEW(get()!!.context).also {
    get()!!.addView(it, params(it))
    lambda(ViewScope.get(it))
}
