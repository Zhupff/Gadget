package zhupf.gadget.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.mutable(): MutableLiveData<T> =
    if (this is MutableLiveData) this else throw IllegalStateException("It is not a mutable object.")

fun <T> MutableLiveData<T>.setValueIfNotEqual(newValue: T?) {
    if (value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNotEqual(newValue: T?) {
    if (value != newValue) postValue(newValue)
}

fun <T> MutableLiveData<T>.setValueIfNotNull(newValue: T?) {
    if (newValue != null) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNotNull(newValue: T?) {
    if (newValue != null) postValue(newValue)
}

fun <T> MutableLiveData<T>.setValueIfNotEqualNorNull(newValue: T?) {
    if (value != newValue && newValue != null) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNotEqualNorNull(newValue: T?) {
    if (value != newValue && newValue != null) postValue(newValue)
}