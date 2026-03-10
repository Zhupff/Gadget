package android.view

import gadget.basic.annotation.DslScope

typealias ViewLayoutParams = ViewGroup.LayoutParams

typealias MarginLayoutParams = ViewGroup.MarginLayoutParams

inline fun MarginLayoutParams(
    size: Pair<Int, Int>,
    lambda: (@DslScope MarginLayoutParams).(MarginLayoutParams) -> Unit,
): MarginLayoutParams = MarginLayoutParams(size.first, size.second).apply {
    this.lambda(this)
}

inline fun <V : View> V.marginLayoutParams(
    lambda: (@DslScope MarginLayoutParams).(MarginLayoutParams) -> Unit,
): MarginLayoutParams {
    val lp = layoutParams?.let {
        it as? MarginLayoutParams ?: MarginLayoutParams(it)
    } ?: MarginLayoutParams(context, null)
    lp.lambda(lp)
    layoutParams = lp
    return lp
}
