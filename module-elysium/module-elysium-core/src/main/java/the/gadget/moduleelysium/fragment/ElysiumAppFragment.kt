package the.gadget.moduleelysium.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.auto.service.AutoService
import the.gadget.modulebase.fragment.BaseFragment
import the.gadget.modulebase.fragment.BindingFragment
import the.gadget.moduleelysiumcore.R
import the.gadget.moduleelysiumcore.databinding.ElysiumAppFragmentBinding
import the.gadget.modulehome.HomeApp

class ElysiumAppFragment : BindingFragment<ElysiumAppFragmentBinding>() {

    override fun getLayoutRes(): Int = R.layout.elysium_app_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }
}


@AutoService(HomeApp::class)
class ElysiumApp : HomeApp("elysium", "极乐池", R.drawable.elysium_app_icon) {
    override fun newFragment(): BaseFragment = ElysiumAppFragment()
}