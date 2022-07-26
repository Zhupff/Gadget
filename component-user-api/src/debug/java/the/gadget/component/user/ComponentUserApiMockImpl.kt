package the.gadget.component.user

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.auto.service.AutoService

@AutoService(MockComponentUserApi::class)
class MockComponentUserApiImpl : MockComponentUserApi {

    private val currentUser: MutableLiveData<User> = MutableLiveData()

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
}