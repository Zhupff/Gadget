package the.gadget.activity

import androidx.appcompat.app.HookActivity
import androidx.lifecycle.LiveData
import the.gadget.theme.Scheme
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeContext

abstract class BaseActivity : HookActivity(), ThemeContext {

    override fun getCurrentScheme(): LiveData<Scheme> = ThemeApi.getCurrentScheme()
}