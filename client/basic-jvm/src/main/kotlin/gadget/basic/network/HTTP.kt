package gadget.basic.network

import gadget.basic.tool.Hello
import okhttp3.OkHttpClient
import java.net.InetAddress
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object HTTP : Hello {

    val LOCAL_SERVER_URL = "https://${LocalServer.host}:${LocalServer.port}"

    private val clients: ConcurrentHashMap<String, OkHttpClient> = ConcurrentHashMap()

    fun client(domain: String = ""): OkHttpClient {
        return clients.getOrPut(domain) {
            when (domain) {
                LocalServer.host -> {
                    val trustManager = object : X509TrustManager {
                        override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {
                            if (chain.isEmpty()) {
                                throw CertificateException("Empty server certificate chain")
                            }
                            val decodedCert = Base64.getEncoder().encodeToString(
                                MessageDigest.getInstance("SHA-256").digest(chain[0].publicKey.encoded)
                            )
                            if (decodedCert != LocalServer.cert) {
                                throw CertificateException("Server certificate public key pin mismatch")
                            }
                        }
                        override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) = Unit
                        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
                    }
                    val sslContext = SSLContext.getInstance("TLS").also {
                        it.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
                    }
                    OkHttpClient.Builder()
                        .connectTimeout(16L, TimeUnit.SECONDS)
                        .readTimeout(32L, TimeUnit.SECONDS)
                        .writeTimeout(32L, TimeUnit.SECONDS)
                        .sslSocketFactory(sslContext.socketFactory, trustManager)
                        .dns { host ->
                            if (host == LocalServer.host) {
                                listOf(InetAddress.getByName(LocalServer.ip))
                            } else {
                                okhttp3.Dns.SYSTEM.lookup(host)
                            }
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