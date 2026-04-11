package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import gadget.basic.annotation.DslScope

inline fun <V : ViewGroup> ViewScope<V>.FrameLayout(
    params: (@DslScope FrameLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<FrameLayout>).() -> Unit = {},
): FrameLayout = FrameLayout(get()!!.context).also {
    get()!!.addView(it, params(it))
    lambda(ViewScope.get(it))
}

inline fun <V : View> V.frameLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (FrameLayout.LayoutParams).() -> Unit = {},
): FrameLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? FrameLayout.LayoutParams ?: FrameLayout.LayoutParams(it)
    } ?: FrameLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
