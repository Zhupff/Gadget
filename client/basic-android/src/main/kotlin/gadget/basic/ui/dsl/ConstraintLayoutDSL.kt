package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import gadget.basic.annotation.DSLScope

inline fun ConstraintLayout(
    context: Context,
    params: (@DSLScope ConstraintLayout).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope ConstraintLayout).() -> Unit = {},
): ConstraintLayout = ConstraintLayout(context).scope(params, lambda)

inline fun <P : ViewGroup> P.ConstraintLayout(
    params: (@DSLScope ConstraintLayout).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope ConstraintLayout).() -> Unit = {},
): ConstraintLayout = scope(ConstraintLayout(context), params, lambda)

inline fun <V : View> V.constraintLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (ConstraintLayout.LayoutParams).() -> Unit = {},
): ConstraintLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? ConstraintLayout.LayoutParams ?: ConstraintLayout.LayoutParams(it)
    } ?: ConstraintLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
