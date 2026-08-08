package gadget.basic.kv

import androidx.datastore.core.DataStore

interface DataStoreProvider<T> {

    fun provide(): DataStore<T>
}