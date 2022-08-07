package the.gadget.component.user

import android.graphics.Bitmap

class UserImpl(uid: String, nickname: String, avatar: Bitmap) : User(uid, nickname, avatar) {

    fun setUserAvatar(avatar: Bitmap) = apply { this.avatar = avatar }

    fun setUserNickname(nickname: String) = apply { this.nickname = nickname }
}