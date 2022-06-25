package the.gadget.module.datastore

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import the.gadget.api.DataStoreApi

@AutoService(DataStoreApi::class)
class DataStoreApiImpl : DataStoreApi {
    private val globalDataStore: DataStore<Preferences> by lazy { ApplicationApi.instance.getApplication().globalDataStore }

    private val Application.globalDataStore: DataStore<Preferences> by preferencesDataStore("global")

    private val String.intPreferencesKey     get() = intPreferencesKey(this)
    private val String.longPreferencesKey    get() = longPreferencesKey(this)
    private val String.floatPreferencesKey   get() = floatPreferencesKey(this)
    private val String.doublePreferencesKey  get() = doublePreferencesKey(this)
    private val String.booleanPreferencesKey get() = booleanPreferencesKey(this)
    private val String.stringPreferencesKey  get() = stringPreferencesKey(this)


    override suspend fun setGlobalInt(key: String, value: Int) {
        setValue(globalDataStore, key.intPreferencesKey, value)
    }
    override suspend fun setGlobalLong(key: String, value: Long) {
        setValue(globalDataStore, key.longPreferencesKey, value)
    }
    override suspend fun setGlobalFloat(key: String, value: Float) {
        setValue(globalDataStore, key.floatPreferencesKey, value)
    }
    override suspend fun setGlobalDouble(key: String, value: Double) {
        setValue(globalDataStore, key.doublePreferencesKey, value)
    }
    override suspend fun setGlobalBoolean(key: String, value: Boolean) {
        setValue(globalDataStore, key.booleanPreferencesKey, value)
    }
    override suspend fun setGlobalString(key: String, value: String) {
        setValue(globalDataStore, key.stringPreferencesKey, value)
    }


    override suspend fun getGlobalInt(key: String, defaultValue: Int): Int  =
        getValue(globalDataStore, key.intPreferencesKey, defaultValue)
    override suspend fun getGlobalLong(key: String, defaultValue: Long): Long  =
        getValue(globalDataStore, key.longPreferencesKey, defaultValue)
    override suspend fun getGlobalFloat(key: String, defaultValue: Float): Float  =
        getValue(globalDataStore, key.floatPreferencesKey, defaultValue)
    override suspend fun getGlobalDouble(key: String, defaultValue: Double): Double =
        getValue(globalDataStore, key.doublePreferencesKey, defaultValue)
    override suspend fun getGlobalBoolean(key: String, defaultValue: Boolean): Boolean =
        getValue(globalDataStore, key.booleanPreferencesKey, defaultValue)
    override suspend fun getGlobalString(key: String, defaultValue: String): String =
        getValue(globalDataStore, key.stringPreferencesKey, defaultValue)

    private suspend fun <T> setValue(dataStore: DataStore<Preferences>, key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    private suspend fun <T> getValue(dataStore: DataStore<Preferences>, key: Preferences.Key<T>, defaultValue: T): T {
        var value: T = defaultValue
        dataStore.edit { value = it[key] ?: defaultValue }
        return value
    }
}