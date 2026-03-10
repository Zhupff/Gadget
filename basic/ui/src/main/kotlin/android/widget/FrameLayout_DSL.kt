package android.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

typealias FrameLayoutParams = FrameLayout.LayoutParams

inline fun FrameLayout(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope FrameLayout).(FrameLayout) -> Unit,
): FrameLayout = FrameLayout(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.FrameLayout(
    lp: ViewLayoutParams,
    lambda: (@DslScope FrameLayout).(FrameLayout) -> Unit,
): FrameLayout = FrameLayout(context).also {
    this.addView(it, lp)
    it.lambda(it)
}

inline fun FrameLayoutParams(
    size: Pair<Int, Int>,
    lambda: (@DslScope FrameLayoutParams).(FrameLayoutParams) -> Unit,
): FrameLayoutParams = FrameLayoutParams(size.first, size.second).apply {
    this.lambda(this)
}

inline fun <V : View> V.frameLayoutParams(
    lambda: (@DslScope FrameLayoutParams).(FrameLayoutParams) -> Unit,
): FrameLayoutParams {
    val lp = layoutParams?.let {
        it as? FrameLayoutParams ?: FrameLayoutParams(it)
    } ?: FrameLayoutParams(context, null)
    lp.lambda(lp)
    layoutParams = lp
    return lp
}
