package gadget.component.setting.navigation

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import gadget.component.main.navigation.MainNavOption
import gadget.component.setting.internal.R

class MainNavOptionSetting : MainNavOption.Simple(
    icon = R.drawable.main_nav_option_setting,
    name = R.string.main_nav_option_setting,
) {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.SETTING

    @AutoService(MainNavOption.Provider::class)
    class Provider : MainNavOption.Provider {
        override fun provide(
            context: Context,
            selection: LiveData<MainNavOption.OptionID>
        ): MainNavOption = MainNavOptionSetting()
    }
}