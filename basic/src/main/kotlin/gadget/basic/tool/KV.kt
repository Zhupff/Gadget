package gadget.basic.tool

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.squareup.wire.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class KV<T : Message<T, *>>(
    name: String,
    context: Context,
    override val defaultValue: T,
) : Serializer<T>, ReadWriteProperty<Any, T> {

    companion object {
        private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    private val Context.store: DataStore<T> by dataStore(name, this)

    val store: DataStore<T> = context.store

    var current: T = defaultValue
        private set

    init {
        ioScope.launch {
            store.data.collectLatest {
                current = it
            }
        }
    }

    override suspend fun readFrom(input: InputStream): T {
        return defaultValue.adapter.decode(input)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        defaultValue.adapter.encode(output, t)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return current
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        current = value
        ioScope.launch {
            store.updateData { value }
        }
    }
}