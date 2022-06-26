package the.gadget.user

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import the.gadget.api.apiInstance

interface UserApi {
    companion object {
        val instance: UserApi by lazy { apiInstance(UserApi::class.java) }
    }

    fun getCurrentUser(): LiveData<User>

    suspend fun login()

    suspend fun updateAvatar(avatar: Bitmap)

    suspend fun updateNickname(nickname: String)

    fun showUserInfoPopupDialog(context: Context)
}