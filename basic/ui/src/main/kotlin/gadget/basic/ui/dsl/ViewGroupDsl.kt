package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import gadget.basic.annotation.DslScope

class MarginLayoutParams<V : View>(
    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,
    initializer: (@DslScope ViewGroup.MarginLayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<ViewGroup.MarginLayoutParams, V>(
    initializer, ViewGroup.MarginLayoutParams(size.first, size.second),
)

inline fun <V : View> V.marginLayoutParams(
    lambda: (@DslScope ViewGroup.MarginLayoutParams).(ViewGroup.MarginLayoutParams) -> Unit = {},
): ViewGroup.MarginLayoutParams {
    val lp = this.layoutParams?.let {
        it as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(it)
    } ?: ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    lambda(lp, lp)
    return lp
}
