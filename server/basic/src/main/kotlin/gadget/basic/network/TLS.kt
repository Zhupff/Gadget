package gadget.basic.network

import gadget.basic.config.ServerConfiguration
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.extensions.HashAlgorithm
import io.ktor.network.tls.extensions.SignatureAlgorithm
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EngineSSLConnectorBuilder
import io.ktor.server.engine.sslConnector
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Base64
import javax.security.auth.x500.X500Principal

object TLS {

    private val keyAlias: String = ServerConfiguration.secret

    private val keyPassword: String = ByteArray(32)
        .also { SecureRandom().nextBytes(it) }
        .let { Base64.getUrlEncoder().withoutPadding().encodeToString(it) }

    private val keyStore: KeyStore = buildKeyStore {
        certificate(keyAlias) {
            hash = HashAlgorithm.SHA256
            sign = SignatureAlgorithm.RSA
            keySizeInBits = 2048
            password = keyPassword
            daysValid = 7
            subject = X500Principal("CN=Gadget,O=Gadget")
            domains = listOf("localhost", "gadget")
            ipAddresses = emptyList()
        }
    }

    val certificate: String = Base64.getEncoder().encodeToString(
        MessageDigest.getInstance("SHA-256")
            .digest((keyStore.getCertificate(keyAlias) as X509Certificate).publicKey.encoded)
    )

    fun sslConnect(
        configuration: ApplicationEngine.Configuration,
        builder: EngineSSLConnectorBuilder.() -> Unit
    ) {
        configuration.sslConnector(
            keyAlias = keyAlias,
            keyStore = keyStore,
            keyStorePassword = { keyPassword.toCharArray() },
            privateKeyPassword = { keyPassword.toCharArray() },
            builder = builder
        )
    }
}