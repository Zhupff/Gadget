package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import gadget.basic.annotation.DslScope

open class LayoutParamsDsl<P : ViewGroup.LayoutParams, V : View>(
    private val initializer: (@DslScope P).(V) -> Unit,
    private val layoutParams: P,
) {
    fun init(view: V) {
        if (view.layoutParams !== this.layoutParams) {
            initializer(this.layoutParams, view)
            view.layoutParams = this.layoutParams
        }
    }

    fun init(parent: ViewGroup, view: V) {
        if (view.layoutParams !== this.layoutParams) {
            initializer(this.layoutParams, view)
            parent.addView(view, this.layoutParams)
        }
    }
}