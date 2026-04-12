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
): IMAGE_VIEW = scope(IMAGE_VIEW(context), params, lambda)
