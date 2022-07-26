package the.gadget.component.user

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import the.gadget.api.apiInstance
import the.gadget.api.apiInstanceOrNull

interface ComponentUserApi {
    companion object {
        val instance: ComponentUserApi by lazy {
            apiInstanceOrNull(ComponentUserApi::class.java)
                ?: apiInstance(MockComponentUserApi::class.java)
        }
    }

    fun getCurrentUser(): LiveData<out User>

    suspend fun login()

    suspend fun updateAvatar(avatar: Bitmap)

    suspend fun updateNickname(nickname: String)

    fun showUserInfoPopupDialog(context: Context)
}