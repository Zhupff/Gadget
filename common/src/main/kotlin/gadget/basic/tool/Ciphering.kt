package gadget.basic.tool

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

interface Ciphering {
    fun encrypt(secret: String, bytes: ByteArray): ByteArray
    fun decrypt(secret: String, bytes: ByteArray): ByteArray

    object AES256 : Ciphering {
        private const val IV_SIZE = 12
        private const val TAG_SIZE = 128
        override fun encrypt(secret: String, bytes: ByteArray): ByteArray {
            val iv = ByteArray(IV_SIZE).also { SecureRandom().nextBytes(it) }
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(secret.sha256(), "AES"), GCMParameterSpec(TAG_SIZE, iv))
            return iv + cipher.doFinal(bytes)
        }
        override fun decrypt(secret: String, bytes: ByteArray): ByteArray {
            val iv = bytes.copyOfRange(0, IV_SIZE)
            val encrypted = bytes.copyOfRange(IV_SIZE, bytes.size)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secret.sha256(), "AES"), GCMParameterSpec(TAG_SIZE, iv))
            return cipher.doFinal(encrypted)
        }
    }
}

fun ByteArray.sha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)
fun String.sha256(): ByteArray = toByteArray(Charsets.UTF_8).sha256()
