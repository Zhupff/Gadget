package gadget.basic.network

import gadget.basic.tool.Hello
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object HTTP : Hello {

    private val clients: ConcurrentHashMap<String, OkHttpClient> = ConcurrentHashMap()

    private val retrofits: ConcurrentHashMap<String, Retrofit> = ConcurrentHashMap()

    fun client(domain: String = ""): OkHttpClient {
        if (LocalServer.host == domain && LocalServer.client != null) {
            return clients.getOrPut(domain) {
                LocalServer.client!!
            }
        }
        return clients.getOrPut("default") {
            OkHttpClient.Builder()
                .connectTimeout(16L, TimeUnit.SECONDS)
                .readTimeout(32L, TimeUnit.SECONDS)
                .writeTimeout(32L, TimeUnit.SECONDS)
                .build()
        }
    }

    fun retrofit(domain: String = ""): Retrofit {
        if (LocalServer.host == domain && LocalServer.retrofit != null) {
            return retrofits.getOrPut(domain) {
                LocalServer.retrofit!!
            }
        }
        return retrofits.getOrPut(domain) {
            Retrofit.Builder()
                .client(client(domain))
                .baseUrl("https://${domain}")
                .build()
        }
    }
}