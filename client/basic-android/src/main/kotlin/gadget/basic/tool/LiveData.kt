package gadget.basic.tool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gadget.basic.exception.throws

fun <T> LiveData<T>.mutable(): MutableLiveData<T> = this as? MutableLiveData
    ?: IllegalStateException("It's not a MutableLiveData!").throws()
