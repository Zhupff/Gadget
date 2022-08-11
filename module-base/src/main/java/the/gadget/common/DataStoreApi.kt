package the.gadget.common

import the.gadget.api.globalApi

interface DataStoreApi {
    companion object : DataStoreApi by DataStoreApi::class.globalApi()

    suspend fun setGlobalInt(key: String, value: Int)
    suspend fun setGlobalLong(key: String, value: Long)
    suspend fun setGlobalFloat(key: String, value: Float)
    suspend fun setGlobalDouble(key: String, value: Double)
    suspend fun setGlobalBoolean(key: String, value: Boolean)
    suspend fun setGlobalString(key: String, value: String)

    suspend fun getGlobalInt(key: String, defaultValue: Int): Int
    suspend fun getGlobalLong(key: String, defaultValue: Long): Long
    suspend fun getGlobalFloat(key: String, defaultValue: Float): Float
    suspend fun getGlobalDouble(key: String, defaultValue: Double): Double
    suspend fun getGlobalBoolean(key: String, defaultValue: Boolean): Boolean
    suspend fun getGlobalString(key: String, defaultValue: String): String
}