package the.gadget.component.elysium

import com.google.auto.service.AutoService
import the.gadget.component.elysium.databinding.ElysiumAppFragmentBinding
import the.gadget.component.home.HomeApp
import the.gadget.fragment.BaseFragment
import the.gadget.fragment.BindingFragment

class ElysiumAppFragment : BindingFragment<ElysiumAppFragmentBinding>() {

    override fun getLayoutRes(): Int = R.layout.elysium_app_fragment
}


@AutoService(HomeApp::class)
class ElysiumApp : HomeApp("elysium", "极乐池", R.drawable.elysium_app_icon) {
    override fun newFragment(): BaseFragment = ElysiumAppFragment()
}