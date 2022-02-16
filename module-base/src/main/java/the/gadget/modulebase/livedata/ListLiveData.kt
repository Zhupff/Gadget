package the.gadget.modulebase.livedata

import androidx.lifecycle.LiveData
import the.gadget.modulebase.thread.ThreadApi
import java.util.concurrent.CopyOnWriteArrayList

abstract class ListLiveData<T> : LiveData<List<T>>() {

    protected open val data: MutableList<T> = mutableListOf()

    fun add(item: T) = apply { data.add(item) }

    fun addAll(items: Collection<T>) = apply { data.addAll(items) }

    fun remove(item: T) = apply { data.remove(item) }

    fun clear() = apply { data.clear() }

    override fun getValue(): MutableList<T> = data

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

open class ArrayListLiveData<T>(items: Collection<T> = emptyList()) : ListLiveData<T>() {

    override val data: ArrayList<T> = ArrayList(items)
}

open class CopyOnWriteArrayListLiveData<T>(items: Collection<T> = emptyList()) : ListLiveData<T>() {

    override val data: CopyOnWriteArrayList<T> = CopyOnWriteArrayList(items)
}