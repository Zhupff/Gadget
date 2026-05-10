package gadget.component.setting.navigation

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import gadget.component.main.navigation.MainNavOption
import gadget.component.setting.internal.R

class MainNavOptionSetting (
    private val context: Context,
    private val selection: LiveData<MainNavOption.OptionID>,
) : MainNavOption {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.SETTING

    override val icon: Int = R.drawable.main_nav_option_setting

    override val name: Int = R.string.main_nav_option_setting

    override val view: View? = null

    @AutoService(MainNavOption.Provider::class)
    class Provider : MainNavOption.Provider {
        override fun provide(
            context: Context,
            selection: LiveData<MainNavOption.OptionID>
        ): MainNavOption = MainNavOptionSetting(context, selection)
    }
}