package androidx.recyclerview.widget

import android.content.Context
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

inline fun RecyclerView(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope RecyclerView).(RecyclerView) -> Unit,
): RecyclerView = RecyclerView(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.RecyclerView(
    lp: ViewLayoutParams,
    lambda: (@DslScope RecyclerView).(RecyclerView) -> Unit,
): RecyclerView = RecyclerView(context).also {
    addView(it, lp)
    it.lambda(it)
}
