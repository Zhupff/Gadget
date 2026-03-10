package android.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

typealias HorizontalLinearLayout = LinearLayout

typealias VerticalLinearLayout = LinearLayout

typealias LinearLayoutParams = LinearLayout.LayoutParams

inline fun HorizontalLinearLayout(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope HorizontalLinearLayout).(HorizontalLinearLayout) -> Unit,
): HorizontalLinearLayout = HorizontalLinearLayout(context).apply {
    orientation = LinearLayout.HORIZONTAL
    layoutParams = lp
    this.lambda(this)
}

inline fun VerticalLinearLayout(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope VerticalLinearLayout).(VerticalLinearLayout) -> Unit,
): VerticalLinearLayout = VerticalLinearLayout(context).apply {
    orientation = LinearLayout.VERTICAL
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.HorizontalLinearLayout(
    lp: ViewLayoutParams,
    lambda: (@DslScope HorizontalLinearLayout).(HorizontalLinearLayout) -> Unit,
): HorizontalLinearLayout = HorizontalLinearLayout(this.context).also {
    it.orientation = LinearLayout.HORIZONTAL
    this.addView(it, lp)
    it.lambda(it)
}

inline fun <V : ViewGroup> V.VerticalLinearLayout(
    lp: ViewLayoutParams,
    lambda: (@DslScope VerticalLinearLayout).(VerticalLinearLayout) -> Unit,
): VerticalLinearLayout = VerticalLinearLayout(this.context).also {
    it.orientation = LinearLayout.VERTICAL
    this.addView(it, lp)
    it.lambda(it)
}

inline fun LinearLayoutParams(
    size: Pair<Int, Int>,
    lambda: (@DslScope LinearLayoutParams).(LinearLayoutParams) -> Unit,
): LinearLayoutParams = LinearLayoutParams(size.first, size.second).apply {
    this.lambda(this)
}

inline fun <V : View> V.linearLayoutParams(
    lambda: (@DslScope LinearLayoutParams).(LinearLayoutParams) -> Unit,
): LinearLayoutParams {
    val lp = layoutParams?.let {
        it as? LinearLayoutParams ?: LinearLayoutParams(it)
    } ?: LinearLayoutParams(context, null)
    lp.lambda(lp)
    layoutParams = lp
    return lp
}
