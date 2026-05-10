package gadget.component.main.navigation

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import gadget.component.main.internal.R

@AutoService(MainNavOption.Provider::class)
class MainNavOptionHomeProvider : MainNavOption.Provider {
    override fun provide(
        context: Context,
        selection: LiveData<MainNavOption.OptionID>
    ): MainNavOption = MainNavOptionHome(context, selection)
}

class MainNavOptionHome(
    private val context: Context,
    private val selection: LiveData<MainNavOption.OptionID>,
) : MainNavOption {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.HOME

    override val icon: Int = R.drawable.main_nav_option_home

    override val name: Int = R.string.main_nav_option_home

    override val view: View? = null
}