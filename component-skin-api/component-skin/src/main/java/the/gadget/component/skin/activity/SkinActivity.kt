package the.gadget.component.skin.activity

import android.os.Bundle
import the.gadget.activity.BindingActivity
import the.gadget.component.skin.databinding.SkinActivityBinding
import the.gadget.component.skin.R

class SkinActivity : BindingActivity<SkinActivityBinding>() {

    override fun getLayoutRes(): Int = R.layout.skin_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }
}