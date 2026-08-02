package gadget.basic.network.udp

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object UdpDiscoverProtocol {
    const val MAX_PACKET_SIZE = 1024
    private const val IV_SIZE = 12
    private const val TAG_SIZE = 128

    fun encrypt(secret: String, text: String): String {
        val iv = ByteArray(IV_SIZE).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(secret.sha256(), "AES"), GCMParameterSpec(TAG_SIZE, iv))
        val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(iv + encrypted)
    }

    fun decrypt(secret: String, text: String): String {
        val bytes = Base64.getDecoder().decode(text)
        val iv = bytes.copyOfRange(0, IV_SIZE)
        val encrypted = bytes.copyOfRange(IV_SIZE, bytes.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secret.sha256(), "AES"), GCMParameterSpec(TAG_SIZE, iv))
        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }

    private fun String.sha256(): ByteArray = MessageDigest.getInstance("SHA-256")
        .digest(toByteArray(Charsets.UTF_8))
}

data class UdpDiscoverRequest(
    val clientId: String,
    val secret: String,
)

data class UdpDiscoverResponse(
    val clientId: String,
    val serverId: String,
    val httpPort: Int,
)
