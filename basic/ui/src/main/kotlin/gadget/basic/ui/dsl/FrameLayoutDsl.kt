package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import gadget.basic.annotation.DslScope

inline fun ViewGroup.FrameLayout(
    params: (@DslScope FrameLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope FrameLayout).() -> Unit = {},
): FrameLayout = FrameLayout(context).also {
    addView(it, params(it))
    lambda(it)
}

inline fun View.frameLayoutParams(
    width: Int = layoutParams?.width ?: WRAP_CONTENT,
    height: Int = layoutParams?.height ?: WRAP_CONTENT,
    lambda: (FrameLayout.LayoutParams).() -> Unit = {},
): FrameLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? FrameLayout.LayoutParams ?: FrameLayout.LayoutParams(it)
    } ?: FrameLayout.LayoutParams(width, height)
    lambda(lp)
    return lp
}
