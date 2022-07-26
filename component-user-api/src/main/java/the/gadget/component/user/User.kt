package the.gadget.component.user

import android.graphics.Bitmap

open class User(
    uid: String,
    nickname: String,
    avatar: Bitmap
) {

    var uid: String = uid; protected set

    var nickname: String = nickname; protected set

    var avatar: Bitmap = avatar; protected set
}