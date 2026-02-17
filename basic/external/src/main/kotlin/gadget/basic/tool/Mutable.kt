package gadget.basic.tool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gadget.basic.exception.throws

fun <T> List<T>.mutable(): MutableList<T> = this as? MutableList
    ?: IllegalStateException("It's not a MutableList!").throws()

fun <T> Set<T>.mutable(): MutableSet<T> = this as? MutableSet
    ?: IllegalStateException("It's not a MutableSet!").throws()

fun <K, V> Map<K, V>.mutable(): MutableMap<K, V> = this as? MutableMap
    ?: IllegalStateException("It's not a MutableMap!").throws()

fun <T> LiveData<T>.mutable(): MutableLiveData<T> = this as? MutableLiveData
    ?: IllegalStateException("It's not a MutableLiveData!").throws()
