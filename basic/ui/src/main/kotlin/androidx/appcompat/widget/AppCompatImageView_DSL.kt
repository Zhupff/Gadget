package androidx.appcompat.widget

import android.content.Context
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

inline fun ImageView(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope AppCompatImageView).(AppCompatImageView) -> Unit,
): AppCompatImageView = AppCompatImageView(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.ImageView(
    lp: ViewLayoutParams,
    lambda: (@DslScope AppCompatImageView).(AppCompatImageView) -> Unit,
): AppCompatImageView = AppCompatImageView(context).also {
    addView(it, lp)
    it.lambda(it)
}
