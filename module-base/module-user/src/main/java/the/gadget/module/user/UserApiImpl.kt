package the.gadget.module.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.auto.service.AutoService
import the.gadget.user.User
import the.gadget.user.UserApi

@AutoService(UserApi::class)
class UserApiImpl : UserApi {

    private val currentUser: MutableLiveData<User> = MutableLiveData()

    override fun getCurrentUser(): LiveData<User> = currentUser
}