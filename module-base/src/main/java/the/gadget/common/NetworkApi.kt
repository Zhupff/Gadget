package the.gadget.common

import the.gadget.api.globalApi

interface NetworkApi {
    companion object : NetworkApi by NetworkApi::class.globalApi()

    fun <T> createHttpInterface(cls: Class<T>): T
}