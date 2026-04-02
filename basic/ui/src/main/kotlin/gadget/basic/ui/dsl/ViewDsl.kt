package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
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

inline fun View(
    context: Context,
    params: LayoutParamsDsl<*, View>,
    lambda: (@DslScope View).(View) -> Unit = {},
): View = View(context).apply {
    params.init(this)
    lambda(this, this)
}

inline fun <L : ViewGroup> L.View(
    params: LayoutParamsDsl<*, View>,
    lambda: (@DslScope View).(View) -> Unit = {},
): View = View(context).also {
    params.init(this, it)
    lambda(it, it)
}

inline fun View.onLayout(crossinline action: (l1: Int, t1: Int, r1: Int, b1: Int, l2: Int, t2: Int, r2: Int, b2: Int) -> Unit): View.OnLayoutChangeListener =
    View.OnLayoutChangeListener { _, l1, t1, r1, b1, l2, t2, r2, b2 ->
        action(l1, t1, r1, b1, l2, t2, r2, b2)
    }.also(this::addOnLayoutChangeListener)
