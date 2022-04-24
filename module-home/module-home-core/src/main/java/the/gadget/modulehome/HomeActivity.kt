package the.gadget.modulehome

import the.gadget.modulebase.activity.BindingActivity
import the.gadget.modulehomecore.databinding.HomeActivityBinding
import the.gadget.modulehomecore.R

class HomeActivity : BindingActivity<HomeActivityBinding>() {
    override fun getLayoutRes(): Int = R.layout.home_activity
}