package the.gadget.livedata

import androidx.lifecycle.LiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashSet

abstract class SetLiveData<T> : LiveData<Set<T>>() {

    protected open val data: MutableSet<T> = mutableSetOf()

    fun size(): Int = data.size

    fun add(item: T) = apply { data.add(item) }

    fun addAll(allItem: Collection<T>) = apply { data.addAll(allItem) }

    fun remove(item: T) = apply { data.remove(item) }

    fun clear() = apply { data.clear() }

    fun first(): T = data.first()

    fun firstOrNull(): T? = data.firstOrNull()

    fun last(): T = data.first()

    fun lastOrNull(): T? = data.lastOrNull()

    override fun getValue(): MutableSet<T> = data

    fun commit() { MainScope().launch { setValue(data) } }
}

class HashSetLiveData<T>(items: Collection<T> = emptyList()) : SetLiveData<T>() {
    override val data: HashSet<T> = HashSet<T>(items)
}

class TreeSetLiveData<T>(items: Collection<T> = emptyList()) : SetLiveData<T>() {

    override val data: TreeSet<T> = TreeSet<T>(items)
}

fun <T> Set<T>.toTreeSetLiveData(): TreeSetLiveData<T> = TreeSetLiveData(this)