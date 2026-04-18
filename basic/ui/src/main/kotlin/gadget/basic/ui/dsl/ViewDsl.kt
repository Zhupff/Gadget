package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
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

inline fun View.onLayout(crossinline action: (l1: Int, t1: Int, r1: Int, b1: Int, l2: Int, t2: Int, r2: Int, b2: Int) -> Unit): View.OnLayoutChangeListener =
    View.OnLayoutChangeListener { _, l1, t1, r1, b1, l2, t2, r2, b2 ->
        action(l1, t1, r1, b1, l2, t2, r2, b2)
    }.also(this::addOnLayoutChangeListener)

inline fun View.OnAttachStateChanged(
    crossinline onAttached: () -> Unit = {},
    crossinline onDetached: () -> Unit = {},
): View.OnAttachStateChangeListener = object : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(view: View) {
        if (view === this@OnAttachStateChanged) {
            onAttached()
        }
    }
    override fun onViewDetachedFromWindow(view: View) {
        if (view === this@OnAttachStateChanged) {
            onDetached()
        }
    }
}.also(this::addOnAttachStateChangeListener)
