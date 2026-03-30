package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import gadget.basic.annotation.DslScope

class MarginLayoutParams<V : View>(
    size: Pair<Int, Int>,
    initializer: (@DslScope ViewGroup.MarginLayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<ViewGroup.MarginLayoutParams, V>(
    initializer, ViewGroup.MarginLayoutParams(size.first, size.second),
)

fun <L : ViewGroup, V : View> L.LayoutParmas(
    size: Pair<Int, Int>,
    initializer: (@DslScope ViewGroup.MarginLayoutParams).(V) -> Unit = {},
): MarginLayoutParams<V> = MarginLayoutParams(size, initializer)

inline fun <V : View> V.marginLayoutParams(
    lambda: (@DslScope ViewGroup.MarginLayoutParams).(ViewGroup.MarginLayoutParams) -> Unit = {},
): ViewGroup.MarginLayoutParams {
    val lp = this.layoutParams?.let {
        it as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(it)
    } ?: ViewGroup.MarginLayoutParams(context, null)
    lambda(lp, lp)
    return lp
}
