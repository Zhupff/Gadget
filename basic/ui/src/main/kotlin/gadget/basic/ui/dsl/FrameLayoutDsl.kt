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
    ensureViewId()
}

inline fun <L : ViewGroup> L.FrameLayout(
    params: LayoutParamsDsl<*, FrameLayout>,
    lambda: (@DslScope FrameLayout).(FrameLayout) -> Unit = {},
): FrameLayout = FrameLayout(context).also {
    params.init(it)
    lambda(it, it)
    addView(it)
    it.ensureViewId()
}

class FrameLayoutParams<V : View>(
    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,
    initializer: (@DslScope FrameLayout.LayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<FrameLayout.LayoutParams, V>(
    initializer, FrameLayout.LayoutParams(size.first, size.second),
)

fun <L : ViewGroup, V : View> L.LayoutParams(
    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,
    initializer: (@DslScope FrameLayout.LayoutParams).(V) -> Unit = {},
): FrameLayoutParams<V> = FrameLayoutParams(size, initializer)

inline fun <V : View> V.frameLayoutParams(
    lambda: (@DslScope FrameLayout.LayoutParams).(FrameLayout.LayoutParams) -> Unit = {},
): FrameLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? FrameLayout.LayoutParams ?: FrameLayout.LayoutParams(it)
    } ?: FrameLayout.LayoutParams(context, null)
    lambda(lp, lp)
    return lp
}
