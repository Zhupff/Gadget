package the.gadget.api

interface NetworkApi {
    companion object {
        val instance: NetworkApi by lazy { apiInstance(NetworkApi::class.java) }
    }

    fun <T> createHttpInterface(cls: Class<T>): T
}