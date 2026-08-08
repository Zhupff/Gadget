package gadget.basic.kv

import androidx.datastore.core.Serializer
import com.squareup.wire.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.InputStream
import java.io.OutputStream

class ProtoSerializer<T : Message<T, *>>(
    override val defaultValue: T,
) : Serializer<T> {

    companion object {
        val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    override suspend fun readFrom(input: InputStream): T {
        return defaultValue.adapter.decode(input)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        defaultValue.adapter.encode(output, t)
    }
}