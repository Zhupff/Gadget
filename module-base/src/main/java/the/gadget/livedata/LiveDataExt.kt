@file:JvmName("LiveDataExt")
package the.gadget.livedata

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import the.gadget.weight.listener.ViewOnAttachStateChangeListener

fun <T> LiveData<T>.observeLifecycleOrForever(lifecycleOwner: LifecycleOwner?, observer: Observer<T>) {
    if (lifecycleOwner != null) observe(lifecycleOwner, observer)
    else observeForever(observer)
}

fun <T> LiveData<T>.observe(view: View, observer: Observer<T>) {
    val lifecycleOwner = view.findViewTreeLifecycleOwner()
    if (lifecycleOwner != null) {
        observe(lifecycleOwner, observer)
    } else {
        observeForever(observer)
        view.addOnAttachStateChangeListener(object : ViewOnAttachStateChangeListener() {
            var observed: Boolean = true
            override fun onViewAttachedToWindow(v: View?) {
                super.onViewAttachedToWindow(v)
                if (!observed) {
                    observed = true
                    observeForever(observer)
                }
            }
            override fun onViewDetachedFromWindow(v: View?) {
                super.onViewDetachedFromWindow(v)
                if (observed) {
                    observed = false
                    removeObserver(observer)
                }
            }
        })
    }
}