package gadget.basic.ui.dsl

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import gadget.basic.annotation.DslScope

//private typealias IMAGE_VIEW = ImageView
private typealias IMAGE_VIEW = AppCompatImageView


inline fun ViewGroup.ImageView(
    params: (@DslScope IMAGE_VIEW).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope IMAGE_VIEW).() -> Unit = {},
): IMAGE_VIEW = IMAGE_VIEW(context).also {
    addView(it, params(it))
    lambda(it)
}
