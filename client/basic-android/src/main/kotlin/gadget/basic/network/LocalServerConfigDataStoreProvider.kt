package gadget.basic.network

import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import com.google.auto.service.AutoService
import gadget.App
import gadget.basic.kv.ProtoSerializer

@AutoService(ILocalServiceConfigDataStoreProvider::class)
internal class LocalServerConfigDataStoreProvider : ILocalServiceConfigDataStoreProvider {

    private companion object : DataStore<LocalServerConfig> by MultiProcessDataStoreFactory.create(
        serializer = ProtoSerializer(LocalServerConfig()),
        scope = ProtoSerializer.ioScope,
        produceFile = { App.instance.configDir.resolve("local_server_config.pb") }
    )

    override fun provide(): DataStore<LocalServerConfig> = LocalServerConfigDataStoreProvider
}