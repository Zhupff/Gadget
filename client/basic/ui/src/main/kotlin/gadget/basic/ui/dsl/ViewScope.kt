package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import gadget.basic.annotation.DSLScope

inline fun <V : View> V.scope(
    params: (@DSLScope V).() -> ViewLayoutParams,
    lambda: (@DSLScope V).() -> Unit = {},
): V {
    layoutParams = params(this)
    lambda(this)
    return this
}

inline fun <P : ViewGroup, V : View> P.scope(
    view: V,
    params: (@DSLScope V).() -> ViewLayoutParams,
    lambda: (@DSLScope V).() -> Unit = {},
): V {
    addView(view, params(view))
    lambda(view)
    return view
}
