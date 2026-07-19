package gadget.basic.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

var BASE_URL = "" // todo, temporary

val HTTP: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(16L, TimeUnit.SECONDS)
        .readTimeout(32L, TimeUnit.SECONDS)
        .writeTimeout(32L, TimeUnit.SECONDS)
        .build()
}

val RETROFIT: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(HTTP)
        .build()
}
