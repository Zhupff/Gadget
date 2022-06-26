package the.gadget.module.user

import android.graphics.Bitmap
import android.graphics.Canvas
import the.gadget.user.User

class UserImpl(uid: String, nickname: String, avatar: Bitmap) : User(uid, nickname, avatar) {
    companion object {
        val DEFAULT_USER: UserImpl by lazy {
            val uid = System.currentTimeMillis()
            val nickname = "User${uid}"
            val avatar = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(avatar)
            canvas.drawColor(uid.toInt())
            UserImpl(uid.toString(), nickname, avatar)
        }
    }
}