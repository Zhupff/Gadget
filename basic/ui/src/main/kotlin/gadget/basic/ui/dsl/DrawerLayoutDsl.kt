package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.drawerlayout.widget.DrawerLayout
import gadget.basic.annotation.DslScope

class DrawerLayoutParams<V : View>(
    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,
    initializer: (@DslScope DrawerLayout.LayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<DrawerLayout.LayoutParams, V>(
    initializer, DrawerLayout.LayoutParams(size.first, size.second),
)

fun <L : DrawerLayout, V : View> L.LayoutParmas(
    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,
    initializer: (@DslScope DrawerLayout.LayoutParams).(V) -> Unit = {},
): DrawerLayoutParams<V> = DrawerLayoutParams(size, initializer)

inline fun <V : View> V.drawerLayoutParams(
    lambda: (@DslScope DrawerLayout.LayoutParams).(DrawerLayout.LayoutParams) -> Unit,
): DrawerLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? DrawerLayout.LayoutParams ?: DrawerLayout.LayoutParams(it)
    } ?: DrawerLayout.LayoutParams(context, null)
    lambda(lp, lp)
    return lp
}
