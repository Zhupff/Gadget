package gadget.basic.network

import gadget.basic.tool.Hello
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object HTTP : Hello {

    private val clients: ConcurrentHashMap<String, OkHttpClient> = ConcurrentHashMap()

    fun client(domain: String = ""): OkHttpClient {
        return clients.getOrPut(domain) {
            when (domain) {
                ILocalServiceConfigDataStoreProvider.serverHost -> {
                    OkHttpClient.Builder()
                        .connectTimeout(16L, TimeUnit.SECONDS)
                        .readTimeout(32L, TimeUnit.SECONDS)
                        .writeTimeout(32L, TimeUnit.SECONDS)
                        .addInterceptor { chain ->
                            val oldRequest = chain.request()
                            val oldUrl = oldRequest.url
                            if (oldUrl.host != ILocalServiceConfigDataStoreProvider.serverHost) {
                                return@addInterceptor chain.proceed(oldRequest)
                            }
                            val newUrl = oldUrl.newBuilder()
                                .scheme("https")
                                .host(ILocalServiceConfigDataStoreProvider.serverHost)
                                .port(ILocalServiceConfigDataStoreProvider.serverPort)
                                .build()
                            val newRequest = oldRequest.newBuilder()
                                .url(newUrl)
                                .build()
                            return@addInterceptor chain.proceed(newRequest)
                        }
                        .build()
                }
                else -> {
                    OkHttpClient.Builder()
                        .connectTimeout(16L, TimeUnit.SECONDS)
                        .readTimeout(32L, TimeUnit.SECONDS)
                        .writeTimeout(32L, TimeUnit.SECONDS)
                        .build()
                }
            }
        }
    }
}