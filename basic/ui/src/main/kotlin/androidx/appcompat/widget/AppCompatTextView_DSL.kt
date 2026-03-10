package androidx.appcompat.widget

import android.content.Context
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

inline fun TextView(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope AppCompatTextView).(AppCompatTextView) -> Unit,
): AppCompatTextView = AppCompatTextView(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.TextView(
    lp: ViewLayoutParams,
    lambda: (@DslScope AppCompatTextView).(AppCompatTextView) -> Unit,
): AppCompatTextView = AppCompatTextView(context).also {
    addView(it, lp)
    it.lambda(it)
}