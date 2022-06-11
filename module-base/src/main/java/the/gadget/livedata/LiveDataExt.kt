@file:JvmName("LiveDataExt")
package the.gadget.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeLifecycleOrForever(lifecycleOwner: LifecycleOwner?, observer: Observer<T>) {
    if (lifecycleOwner != null) observe(lifecycleOwner, observer)
    else observeForever(observer)
}