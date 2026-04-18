package gadget.component.main.navigation

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData

interface MainNavOption {

    interface Provider {
        fun provide(context: Context, selection: LiveData<OptionID>): MainNavOption
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

    val view: View

    fun onClick(): Boolean = true
}