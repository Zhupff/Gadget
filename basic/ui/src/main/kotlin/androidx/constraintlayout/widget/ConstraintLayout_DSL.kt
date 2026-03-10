package androidx.constraintlayout.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

typealias ConstraintLayoutParams = ConstraintLayout.LayoutParams

inline fun ConstraintLayout(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope ConstraintLayout).(ConstraintLayout) -> Unit,
): ConstraintLayout = ConstraintLayout(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.ConstraintLayout(
    lp: ViewLayoutParams,
    lambda: (@DslScope ConstraintLayout).(ConstraintLayout) -> Unit,
): ConstraintLayout = ConstraintLayout(context).also {
    this.addView(it, lp)
    it.lambda(it)
}

inline fun ConstraintLayoutParams(
    size: Pair<Int, Int>,
    lambda: (@DslScope ConstraintLayoutParams).(ConstraintLayoutParams) -> Unit,
): ConstraintLayoutParams = ConstraintLayoutParams(size.first, size.second).apply {
    this.lambda(this)
}

inline fun <V : View> V.constraintLayoutParams(
    lambda: (@DslScope ConstraintLayoutParams).(ConstraintLayoutParams) -> Unit,
): ConstraintLayoutParams {
    val lp = layoutParams?.let {
        it as? ConstraintLayoutParams ?: ConstraintLayoutParams(it)
    } ?: ConstraintLayoutParams(context, null)
    lp.lambda(lp)
    layoutParams = lp
    return lp
}
