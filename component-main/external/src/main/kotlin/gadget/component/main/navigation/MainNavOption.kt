package gadget.component.main.navigation

import android.content.Context
import android.util.NoSuchPropertyException
import android.view.View
import androidx.lifecycle.LiveData
import gadget.basic.exception.throws

interface MainNavOption {

    interface Provider {
        fun provide(context: Context, selection: LiveData<OptionID>): MainNavOption
    }

    abstract class Simple(
        /** drawable res id */
        val icon: Int,
        /** string res id */
        val name: Int,
    ) : MainNavOption {
        override val view: View
            get() = NoSuchPropertyException("Use SimpleItemView instead!").throws()
    }

    enum class OptionID {
        HOME,
        PROXY,
        ROLE,
        SETTING,
        ABOUT,
        ;
    }

    val id: OptionID

    val view: View

    fun onClick(): Boolean = true
}