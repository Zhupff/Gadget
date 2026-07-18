package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import gadget.basic.annotation.DSLScope

internal typealias ViewLayoutParams = ViewGroup.LayoutParams

inline fun <V : ViewGroup> V.View(
    params: (@DSLScope View).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope View).() -> Unit = {},
): View = scope(View(context), params, lambda)

inline fun <V : View> V.marginLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (ViewGroup.MarginLayoutParams).() -> Unit = {},
): ViewGroup.MarginLayoutParams {
    val lp = this.layoutParams?.let {
        it as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(it)
    } ?: ViewGroup.MarginLayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}

object ViewId {
    operator fun component1(): Int = View.generateViewId()
    operator fun component2(): Int = View.generateViewId()
    operator fun component3(): Int = View.generateViewId()
    operator fun component4(): Int = View.generateViewId()
    operator fun component5(): Int = View.generateViewId()
    operator fun component6(): Int = View.generateViewId()
    operator fun component7(): Int = View.generateViewId()
    operator fun component8(): Int = View.generateViewId()
    operator fun component9(): Int = View.generateViewId()
}
