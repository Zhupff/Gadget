package the.gadget.component.theme

import android.os.Bundle
import the.gadget.activity.BindingActivity
import the.gadget.component.theme.databinding.ThemeActivityBinding

class ThemeActivity : BindingActivity<ThemeActivityBinding>() {

    override fun getLayoutRes(): Int = R.layout.theme_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }
}