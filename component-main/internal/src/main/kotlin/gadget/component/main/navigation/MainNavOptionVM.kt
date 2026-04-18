package gadget.component.main.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gadget.basic.tool.iteration
import gadget.basic.tool.mutable

class MainNavOptionVM : ViewModel() {

    val current: LiveData<MainNavOption.OptionID> = MutableLiveData(MainNavOption.OptionID.HOME)

    val allProviders: List<MainNavOption.Provider> = iteration()

    fun select(option: MainNavOption) {
        if (option.id !== current.value) {
            current.mutable().postValue(option.id)
        }
    }
}