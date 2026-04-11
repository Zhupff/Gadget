package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import gadget.basic.annotation.DslScope

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

inline fun <V : ViewGroup> ViewScope<V>.View(
    params: (@DslScope View).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<View>).() -> Unit = {},
): View = View(get()!!.context).also {
    get()!!.addView(it, params(it))
    lambda(ViewScope.get(it))
}

inline fun View.marginLayoutParams(
    width: Int = layoutParams?.width ?: WRAP_CONTENT,
    height: Int = layoutParams?.height ?: WRAP_CONTENT,
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

inline fun View.onLayout(crossinline action: (l1: Int, t1: Int, r1: Int, b1: Int, l2: Int, t2: Int, r2: Int, b2: Int) -> Unit): View.OnLayoutChangeListener =
    View.OnLayoutChangeListener { _, l1, t1, r1, b1, l2, t2, r2, b2 ->
        action(l1, t1, r1, b1, l2, t2, r2, b2)
    }.also(this::addOnLayoutChangeListener)
