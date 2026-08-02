package gadget.basic.network.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object HTTP {

    private const val TEMP_HOST = "alyx.gadget"

    private val hostLatch = CountDownLatch(1)

    var BASE_URL: String = TEMP_HOST
        set(value) {
            if (hostLatch.count <= 0) {
                return
            } else {
                if (field != value) {
                    field = value
                    hostLatch.countDown()
                }
            }
        }
        get() {
            hostLatch.await()
            return field
        }

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(16L, TimeUnit.SECONDS)
            .readTimeout(32L, TimeUnit.SECONDS)
            .writeTimeout(32L, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val oldRequest = chain.request()
                if (oldRequest.url.host != TEMP_HOST) {
                    return@addInterceptor chain.proceed(oldRequest)
                }
                val newRequest = oldRequest.newBuilder()
                    .url(BASE_URL)
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://${TEMP_HOST}/")
            .client(client)
            .build()
    }
}
