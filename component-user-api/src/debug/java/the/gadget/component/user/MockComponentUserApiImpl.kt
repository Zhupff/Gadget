package the.gadget.component.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.api.GlobalApi

@GlobalApi(ComponentUserApi::class, lazy = false)
class MockComponentUserApiImpl : ComponentUserApi {

    private val currentUser: MutableLiveData<User> = MutableLiveData()

    init {
        CoroutineScope(Dispatchers.IO).launch { login() }
    }

    override fun getCurrentUser(): LiveData<User> = currentUser

    override suspend fun login() {
        if (currentUser.value != null) return
        val uid = System.currentTimeMillis()
        val nickname = "User${uid % 10000}"
        val avatar = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888).also {
            Canvas(it).drawColor(uid.toInt())
        }
        currentUser.postValue(User(uid.toString(), nickname, avatar))
    }

    override suspend fun updateAvatar(avatar: Bitmap) {}

    override suspend fun updateNickname(nickname: String) {}

    override fun showUserInfoPopupDialog(context: Context) {}
}