package the.gadget.moduleskin.activity

import android.os.Bundle
import the.gadget.modulebase.activity.BindingActivity
import the.gadget.moduleskincore.R
import the.gadget.moduleskincore.databinding.SkinActivityBinding

class SkinActivity : BindingActivity<SkinActivityBinding>() {

    override fun getLayoutRes(): Int = R.layout.skin_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }
}