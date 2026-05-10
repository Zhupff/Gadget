package gadget.component.proxy.navigation

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import gadget.basic.tool.singleToastS
import gadget.component.main.navigation.MainNavOption
import gadget.component.proxy.internal.R

class MainNavOptionProxy(
    private val context: Context,
    private val selection: LiveData<MainNavOption.OptionID>,
)  : MainNavOption.Simple(
    icon = R.drawable.main_nav_option_proxy_off,
    name = R.string.main_nav_option_proxy,
) {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.PROXY

    override fun onClick(): Boolean {
        gadget.basic.R.string.feature_not_supported.singleToastS()
        return false
    }

    @AutoService(MainNavOption.Provider::class)
    class Provider : MainNavOption.Provider {
        override fun provide(
            context: Context,
            selection: LiveData<MainNavOption.OptionID>
        ): MainNavOption = MainNavOptionProxy(context, selection)
    }
}