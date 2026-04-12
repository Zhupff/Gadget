package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.widget.Toolbar
import gadget.basic.annotation.DslScope

inline fun <V : ViewGroup> ViewScope<V>.Toolbar(
    params: (@DslScope Toolbar).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<Toolbar>).() -> Unit = {},
): Toolbar = scope(Toolbar(context), params, lambda)

inline fun <V : View> V.toolbarLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (Toolbar.LayoutParams).() -> Unit = {},
): Toolbar.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? Toolbar.LayoutParams ?: Toolbar.LayoutParams(it)
    } ?: Toolbar.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
