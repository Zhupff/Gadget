package gadget.component.main.navigation

import com.google.auto.service.AutoService
import gadget.component.main.internal.R

@AutoService(MainNavOption::class)
class MainNavOptionHome : MainNavOption {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.HOME

    override val icon: Int = R.drawable.main_nav_option_home

    override val name: Int = R.string.main_nav_option_home
}