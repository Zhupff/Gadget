package the.gadget.user

import androidx.lifecycle.LiveData
import the.gadget.api.apiInstance

interface UserApi {
    companion object {
        val instance: UserApi by lazy { apiInstance(UserApi::class.java) }
    }

    fun getCurrentUser(): LiveData<User>
}