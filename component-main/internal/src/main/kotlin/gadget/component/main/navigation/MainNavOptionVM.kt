package gadget.component.main.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gadget.basic.tool.mutable

class MainNavOptionVM : ViewModel() {

    val current: LiveData<MainNavOption> = MutableLiveData(
        MainNavOption.ALL.find { it.id == MainNavOption.OptionID.HOME }!!
    )

    fun select(option: MainNavOption) {
        if (option != current.value) {
            current.mutable().postValue(option)
        }
    }
}