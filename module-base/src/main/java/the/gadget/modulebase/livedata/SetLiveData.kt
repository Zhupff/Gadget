package the.gadget.modulebase.livedata

import androidx.lifecycle.LiveData
import the.gadget.modulebase.thread.ThreadApi
import java.util.*
import kotlin.collections.HashSet

abstract class SetLiveData<T> : LiveData<Set<T>>() {

    protected open val data: MutableSet<T> = mutableSetOf()

    fun add(item: T) = apply { data.add(item) }

    fun addAll(items: Collection<T>) = apply { data.addAll(items) }

    fun remove(item: T) = apply { data.remove(item) }

    fun clear() = apply { data.clear() }

    override fun getValue(): MutableSet<T> = data

    fun setValue() {
        setValue(data)
    }

    fun postValue() {
        postValue(data)
    }

    fun commit() {
        if (ThreadApi.instance.isOnMainThread()) {
            setValue()
        } else {
            postValue()
        }
    }
}

class HashSetLiveData<T>(items: Collection<T> = emptyList()) : SetLiveData<T>() {
    override val data: HashSet<T> = HashSet<T>(items)
}

class TreeSetLiveData<T>(items: Collection<T> = emptyList()) : SetLiveData<T>() {

    override val data: TreeSet<T> = TreeSet<T>(items)
}

fun <T> Set<T>.toTreeSetLiveData(): TreeSetLiveData<T> = TreeSetLiveData(this)