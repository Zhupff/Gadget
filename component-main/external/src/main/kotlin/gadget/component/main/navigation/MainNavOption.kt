package gadget.component.main.navigation

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gadget.basic.tool.iteration

interface MainNavOption {
    companion object {
        val all: List<MainNavOption> = iteration<MainNavOption>().sortedBy { it.id }
        val current: LiveData<MainNavOption> = MutableLiveData(all.find { it.id == OptionID.HOME })
    }

    enum class OptionID {
        HOME,
        VIDEO,
        AUDIO,
        ROLE,
        SETTING,
        ABOUT,
        ;
    }

    val id: OptionID
    /** drawable res id */
    val icon: Int
    /** string res id */
    val name: Int

    fun createView(parent: ViewGroup): View? = null

    fun onClick(): Boolean = true
}