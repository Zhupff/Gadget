package the.gadget

import android.os.Bundle
import the.gadget.databinding.MainActivityBinding
import the.gadget.modulebase.activity.BindingActivity

class MainActivity : BindingActivity<MainActivityBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }

    override fun getLayoutRes(): Int = R.layout.main_activity
}