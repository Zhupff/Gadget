package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.drawerlayout.widget.DrawerLayout

inline fun <V : View> V.drawerLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (DrawerLayout.LayoutParams).() -> Unit = {},
): DrawerLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? DrawerLayout.LayoutParams ?: DrawerLayout.LayoutParams(it)
    } ?: DrawerLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
