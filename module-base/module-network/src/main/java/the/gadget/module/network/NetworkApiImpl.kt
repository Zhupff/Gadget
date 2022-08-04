package the.gadget.module.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import the.gadget.api.GlobalApi
import the.gadget.network.HttpInterface
import the.gadget.common.NetworkApi

@GlobalApi(NetworkApi::class)
class NetworkApiImpl : NetworkApi {

    override fun <T> createHttpInterface(cls: Class<T>): T {
        val httpInterface = cls.getAnnotation(HttpInterface::class.java)
            ?: throw IllegalStateException("$cls isn't annotated with ${HttpInterface::class.java}.")
        return Retrofit.Builder()
            .baseUrl(httpInterface.value)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(cls)
    }
}