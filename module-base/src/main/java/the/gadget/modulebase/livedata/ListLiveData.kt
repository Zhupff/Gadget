package the.gadget.modulebase.livedata

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

abstract class ListLiveData<T> : LiveData<List<T>>() {

    protected open val data: MutableList<T> = mutableListOf()

    fun size(): Int = data.size

    fun add(item: T) = apply { data.add(item) }

    fun addAll(allItem: Collection<T>) = apply { data.addAll(allItem) }

    fun remove(item: T) = apply { data.remove(item) }

    fun clear() = apply { data.clear() }

    fun first(): T = data.first()

    fun firstOrNull(): T? = data.firstOrNull()

    fun last(): T = data.first()

    fun lastOrNull(): T? = data.lastOrNull()

    fun lastIndex(): Int = data.lastIndex

    operator fun get(index: Int): T = data[index]

    override fun getValue(): MutableList<T> = data

    fun commit() { CoroutineScope(Dispatchers.Main).launch { setValue(data) } }
}

open class ArrayListLiveData<T>(items: Collection<T> = emptyList()) : ListLiveData<T>() {

    override val data: ArrayList<T> = ArrayList(items)
}

open class CopyOnWriteArrayListLiveData<T>(items: Collection<T> = emptyList()) : ListLiveData<T>() {

    override val data: CopyOnWriteArrayList<T> = CopyOnWriteArrayList(items)
}


fun <T> List<T>.toArrayListLiveData(): ArrayListLiveData<T> = ArrayListLiveData(this)