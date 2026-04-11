package gadget.basic.ui.common

import android.view.View
import android.view.ViewGroup

fun View.moveTo(container: ViewGroup) {
    val currentContainer = parent as? ViewGroup
    if (currentContainer !== container) {
        currentContainer?.removeView(this)
        container.addView(this)
    }
}
