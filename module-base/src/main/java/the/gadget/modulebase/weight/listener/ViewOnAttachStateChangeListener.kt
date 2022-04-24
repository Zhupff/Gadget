package the.gadget.modulebase.weight.listener

import android.view.View

open class ViewOnAttachStateChangeListener : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(v: View?) {}
    override fun onViewDetachedFromWindow(v: View?) {}
}