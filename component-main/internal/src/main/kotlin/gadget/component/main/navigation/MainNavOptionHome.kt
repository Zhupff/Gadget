package gadget.component.main.navigation

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import gadget.component.main.internal.R

class MainNavOptionHome : MainNavOption.Simple(
    icon = R.drawable.main_nav_option_home,
    name = R.string.main_nav_option_home,
) {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.HOME

    @AutoService(MainNavOption.Provider::class)
    class Provider : MainNavOption.Provider {
        override fun provide(
            context: Context,
            selection: LiveData<MainNavOption.OptionID>
        ): MainNavOption = MainNavOptionHome()
    }
}