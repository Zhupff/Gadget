package android.view

import android.content.Context
import gadget.basic.annotation.DslScope

inline fun View(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope View).(View) -> Unit,
): View = View(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.View(
    lp: ViewLayoutParams,
    lambda: (@DslScope View).(View) -> Unit,
): View = View(context).also {
    addView(it, lp)
    it.lambda(it)
}
