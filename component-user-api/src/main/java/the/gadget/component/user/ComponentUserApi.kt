package the.gadget.component.user

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import the.gadget.api.globalApi

interface ComponentUserApi {
    companion object {
        val instance: ComponentUserApi by lazy { ComponentUserApi::class.globalApi() }
    }

    fun getCurrentUser(): LiveData<out User>

    suspend fun login()

    suspend fun updateAvatar(avatar: Bitmap)

    suspend fun updateNickname(nickname: String)

    fun showUserInfoPopupDialog(context: Context)
}