package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import gadget.basic.annotation.DslScope

fun <V : View> V.ensureViewId() {
    if (this.id == View.NO_ID) {
        this.id = View.generateViewId()
    }
}

inline fun View(
    context: Context,
    params: LayoutParamsDsl<*, View>,
    lambda: (@DslScope View).(View) -> Unit = {},
): View = View(context).apply {
    params.init(this)
    lambda(this, this)
    ensureViewId()
}

inline fun <L : ViewGroup> L.View(
    params: LayoutParamsDsl<*, View>,
    lambda: (@DslScope View).(View) -> Unit = {},
): View = View(context).also {
    params.init(it)
    lambda(it, it)
    addView(it)
    it.ensureViewId()
}
