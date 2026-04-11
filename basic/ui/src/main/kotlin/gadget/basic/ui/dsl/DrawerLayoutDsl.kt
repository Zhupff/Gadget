package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.drawerlayout.widget.DrawerLayout

inline fun View.drawerLayoutParams(
    width: Int = layoutParams?.width ?: WRAP_CONTENT,
    height: Int = layoutParams?.height ?: WRAP_CONTENT,
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
