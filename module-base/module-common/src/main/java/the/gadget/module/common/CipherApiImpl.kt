package the.gadget.module.common

import android.util.Base64
import the.gadget.api.GlobalApi
import the.gadget.common.CipherApi
import the.gadget.common.logW
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

@GlobalApi(CipherApi::class)
class CipherApiImpl : CipherApi {
    companion object {
        const val ALGORITHM: String = "DES"
    }

    override fun encrypt(key: String, str: String): String? {
        try {
            val cipher = Cipher.getInstance(ALGORITHM).also {
                it.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance(ALGORITHM).generateSecret(DESKeySpec(key.toByteArray())))
            }
            return Base64.encodeToString(cipher.doFinal(str.toByteArray()), Base64.DEFAULT)
        } catch (e: Exception) {
            logW("encrypt error(${key}, ${str})").logW(e)
        }
        return null
    }

    override fun decrypt(key: String, str: String): String? {
        try {
            val cipher = Cipher.getInstance(ALGORITHM).also {
                it.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance(ALGORITHM).generateSecret(DESKeySpec(key.toByteArray())))
            }
            return String(cipher.doFinal(Base64.decode(str, Base64.DEFAULT)))
        } catch (e: Exception) {
            logW("decrypt error(${key}, ${str})").logW(e)
        }
        return null
    }
}