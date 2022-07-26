package the.gadget.component.user

import android.content.Context
import android.graphics.Bitmap

internal interface MockComponentUserApi : ComponentUserApi {

    override suspend fun updateAvatar(avatar: Bitmap) {}

    override suspend fun updateNickname(nickname: String) {}

    override fun showUserInfoPopupDialog(context: Context) {}
}