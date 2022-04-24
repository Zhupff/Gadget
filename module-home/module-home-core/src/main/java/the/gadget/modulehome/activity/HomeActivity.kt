package the.gadget.modulehome.activity

import android.os.Bundle
import the.gadget.modulebase.activity.BindingActivity
import the.gadget.modulehomecore.databinding.HomeActivityBinding
import the.gadget.modulehomecore.R

class HomeActivity : BindingActivity<HomeActivityBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }

    override fun getLayoutRes(): Int = R.layout.home_activity
}