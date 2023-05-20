package zhupf.gadget.widget.listener

import android.os.SystemClock
import android.view.View

abstract class ViewOnSingleClickListener(var interval: Long = 500L): View.OnClickListener {

    protected var lastClickTime = 0L

    final override fun onClick(v: View?) {
        val currentClickTime = SystemClock.elapsedRealtime()
        if (interval <= 0 || currentClickTime - lastClickTime > interval) {
            lastClickTime = currentClickTime
            onSingleClick(v)
        }
    }

    open fun onSingleClick(v: View?) {}
}