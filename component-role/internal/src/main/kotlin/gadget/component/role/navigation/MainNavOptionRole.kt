package gadget.component.role.navigation

import com.google.auto.service.AutoService
import gadget.component.main.navigation.MainNavOption
import gadget.component.role.internal.R

@AutoService(MainNavOption::class)
class MainNavOptionRole : MainNavOption {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.ROLE

    override val icon: Int = R.drawable.main_nav_option_role

    override val name: Int = R.string.main_nav_option_role
}