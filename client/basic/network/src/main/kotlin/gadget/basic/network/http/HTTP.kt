package gadget.basic.network.http

import gadget.basic.exception.throws
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object HTTP {

    private const val TEMP_HOST = "alyx.gadget"

    private val hostLatch = CountDownLatch(1)

    private var BASE_URL: Triple<String, String, Int> = Triple("http", TEMP_HOST, 9527)
        set(value) {
            if (hostLatch.count <= 0) {
                return
            } else {
                if (field.second != value.second) {
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
                val oldUrl = oldRequest.url
                if (oldUrl.host != TEMP_HOST) {
                    return@addInterceptor chain.proceed(oldRequest)
                }
                val baseUrl = try {
                    runBlocking {
                        withTimeout(5_000L) {
                            BASE_URL
                        }
                    }
                } catch (throwable: Throwable) {
                    throwable.throws("BASE_URL not discovered!")
                }
                val newUrl = oldUrl.newBuilder()
                    .scheme(baseUrl.first)
                    .host(baseUrl.second)
                    .port(baseUrl.third)
                    .build()
                val newRequest = oldRequest.newBuilder()
                    .url(newUrl)
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

    fun updateBaseUrl(scheme: String, host: String, port: Int) {
        BASE_URL = Triple(scheme, host, port)
    }

    fun updateBaseUrl(url: String) {
        val httpUrl = url.toHttpUrl()
        updateBaseUrl(httpUrl.scheme, httpUrl.host, httpUrl.port)
    }
}
