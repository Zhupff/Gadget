package the.gadget.common

import the.gadget.api.globalApi

interface NetworkApi {
    companion object {
        val instance: NetworkApi by lazy { NetworkApi::class.globalApi() }
    }

    fun <T> createHttpInterface(cls: Class<T>): T
}