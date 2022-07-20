package the.gadget.module.network

import com.google.auto.service.AutoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import the.gadget.annotation.HttpInterface
import the.gadget.api.NetworkApi

@AutoService(NetworkApi::class)
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