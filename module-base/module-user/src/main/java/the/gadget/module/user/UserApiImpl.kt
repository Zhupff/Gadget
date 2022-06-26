package the.gadget.module.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.auto.service.AutoService
import the.gadget.api.DataStoreApi
import the.gadget.api.FileApi
import the.gadget.api.ImageApi
import the.gadget.user.User
import the.gadget.user.UserApi
import java.io.File

@AutoService(UserApi::class)
class UserApiImpl : UserApi {
    companion object {
        private const val AVATAR_FILE_NAME: String = "avatar.png"
        private val AVATAR_FILE: File; get() = FileApi.AVATAR_DIR.resolve(AVATAR_FILE_NAME)
        private const val STORE_USER_NICKNAME_KEY: String = "store_user_nickname"
        private const val STORE_USER_ID_KEY: String = "store_user_id"
    }

    private val currentUser: MutableLiveData<User> = MutableLiveData()

    override fun getCurrentUser(): LiveData<User> = currentUser

    override suspend fun login() {
        if (currentUser.value != null) return
        val defaultUser = UserImpl.DEFAULT_USER
        val uid = DataStoreApi.instance.getGlobalString(STORE_USER_ID_KEY, defaultUser.uid)
        val nickname = DataStoreApi.instance.getGlobalString(STORE_USER_NICKNAME_KEY, defaultUser.nickname)
        val avatar = if (AVATAR_FILE.exists()) ImageApi.instance.loadWallpaperBitmap(AVATAR_FILE.path) else defaultUser.avatar
        val user = UserImpl(uid, nickname, avatar)
        currentUser.postValue(user)
    }
}