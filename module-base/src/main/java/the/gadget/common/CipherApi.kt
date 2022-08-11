package the.gadget.common

import the.gadget.api.globalApi

interface CipherApi {
    companion object : CipherApi by CipherApi::class.globalApi()

    fun encrypt(key: String, str: String): String?

    fun decrypt(key: String, str: String): String?
}