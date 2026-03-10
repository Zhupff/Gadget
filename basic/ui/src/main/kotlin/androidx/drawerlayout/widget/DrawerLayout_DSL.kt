package androidx.drawerlayout.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewLayoutParams
import gadget.basic.annotation.DslScope

typealias DrawerLayoutParams = DrawerLayout.LayoutParams

inline fun DrawerLayout(
    context: Context,
    lp: ViewLayoutParams,
    lambda: (@DslScope DrawerLayout).(DrawerLayout) -> Unit,
): DrawerLayout = DrawerLayout(context).apply {
    layoutParams = lp
    this.lambda(this)
}

inline fun <V : ViewGroup> V.DrawerLayout(
    lp: ViewLayoutParams,
    lambda: (@DslScope DrawerLayout).(DrawerLayout) -> Unit,
): DrawerLayout = DrawerLayout(context).also {
    addView(it, lp)
    it.lambda(it)
}

inline fun DrawerLayoutParams(
    size: Pair<Int, Int>,
    lambda: (@DslScope DrawerLayoutParams).(DrawerLayoutParams) -> Unit,
): DrawerLayoutParams = DrawerLayoutParams(size.first, size.second).apply {
    this.lambda(this)
}

inline fun <V : View> V.drawerLayoutParams(
    lambda: (@DslScope DrawerLayoutParams).(DrawerLayoutParams) -> Unit,
): DrawerLayoutParams {
    val lp = layoutParams?.let {
        it as? DrawerLayoutParams ?: DrawerLayoutParams(it)
    } ?: DrawerLayoutParams(context, null)
    lp.lambda(lp)
    layoutParams = lp
    return lp
}
