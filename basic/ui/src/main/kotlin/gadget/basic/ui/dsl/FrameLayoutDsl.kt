package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import gadget.basic.annotation.DslScope

inline fun FrameLayout(
    context: Context,
    params: LayoutParamsDsl<*, FrameLayout>,
    lambda: (@DslScope FrameLayout).(FrameLayout) -> Unit = {},
): FrameLayout = FrameLayout(context).apply {
    params.init(this)
    lambda(this, this)
}

inline fun ViewGroup.FrameLayout(
    params: LayoutParamsDsl<*, FrameLayout>,
    lambda: (@DslScope FrameLayout).(FrameLayout) -> Unit = {},
): FrameLayout = FrameLayout(context).also {
    params.init(this, it)
    lambda(it, it)
}

class FrameLayoutParams<V : View>(
    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,
    initializer: (@DslScope FrameLayout.LayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<FrameLayout.LayoutParams, V>(
    initializer, FrameLayout.LayoutParams(size.first, size.second),
)

inline fun View.frameLayoutParams(
    lambda: (@DslScope FrameLayout.LayoutParams).(FrameLayout.LayoutParams) -> Unit = {},
): FrameLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? FrameLayout.LayoutParams ?: FrameLayout.LayoutParams(it)
    } ?: FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    lambda(lp, lp)
    return lp
}
