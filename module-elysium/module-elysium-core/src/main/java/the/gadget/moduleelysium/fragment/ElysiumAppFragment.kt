package the.gadget.moduleelysium.fragment

import com.google.auto.service.AutoService
import the.gadget.modulebase.fragment.BaseFragment
import the.gadget.modulebase.fragment.BindingFragment
import the.gadget.moduleelysiumcore.R
import the.gadget.moduleelysiumcore.databinding.ElysiumAppFragmentBinding
import the.gadget.modulehome.ModuleHomeApi

class ElysiumAppFragment : BindingFragment<ElysiumAppFragmentBinding>() {

    override fun getLayoutRes(): Int = R.layout.elysium_app_fragment
}


@AutoService(ModuleHomeApi.HomeApp::class)
class ElysiumApp : ModuleHomeApi.HomeApp("elysium", "极乐池", R.drawable.elysium_app_icon) {
    override fun newFragment(): BaseFragment = ElysiumAppFragment()
}