package the.gadget.module.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.auto.service.AutoService
import the.gadget.activity.toBaseActivity
import the.gadget.api.DataStoreApi
import the.gadget.api.FileApi
import the.gadget.api.ImageApi
import the.gadget.fragment.FragmentApi
import the.gadget.user.User
import the.gadget.user.UserApi
import java.io.File

@AutoService(UserApi::class)
class UserApiImpl : UserApi {
    companion object {
        private const val AVATAR_FILE_NAME: String = "avatar.png"
        private val AVATAR_FILE: File; get() = FileApi.AVATAR_DIR.resolve(AVATAR_FILE_NAME)
        private const val STORE_USER_NICKNAME_KEY: String = "store_user_nickname"
    }

    private val currentUser: MutableLiveData<User> = MutableLiveData()

    override fun getCurrentUser(): LiveData<User> = currentUser

    override suspend fun login() {
        if (currentUser.value != null) return
        val uid = System.currentTimeMillis()
        val nickname = DataStoreApi.instance.getGlobalString(STORE_USER_NICKNAME_KEY, "User${uid % 10000}")
        val avatar = if (AVATAR_FILE.exists()) {
            ImageApi.instance.loadAvatarBitmap(AVATAR_FILE.path)
        } else {
            val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
            Canvas(bitmap).drawColor(uid.toInt())
            bitmap
        }
        currentUser.postValue(UserImpl(uid.toString(), nickname, avatar))
    }

    override suspend fun updateAvatar(avatar: Bitmap) {
        currentUser.value?.let { user ->
            FileApi.instance.saveBitmap(avatar, AVATAR_FILE)
            (user as UserImpl).setUserAvatar(avatar)
            currentUser.postValue(user)
        }
    }

    override suspend fun updateNickname(nickname: String) {
        currentUser.value?.let { user ->
            DataStoreApi.instance.setGlobalString(STORE_USER_NICKNAME_KEY, nickname)
            (user as UserImpl).setUserNickname(nickname)
            currentUser.postValue(user)
        }
    }

    override fun showUserInfoPopupDialog(context: Context) {
        context.toBaseActivity()?.supportFragmentManager?.let { fm ->
            FragmentApi.instance.showDialogFragment(fm, UserInfoPopupDialog())
        }
    }
}