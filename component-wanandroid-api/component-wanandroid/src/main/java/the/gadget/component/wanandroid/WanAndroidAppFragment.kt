package the.gadget.component.wanandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.auto.service.AutoService
import the.gadget.component.home.HomeApp
import the.gadget.component.wanandroid.databinding.WanAndroidAppFragmentBinding
import the.gadget.fragment.BaseFragment
import the.gadget.fragment.BindingFragment

class WanAndroidAppFragment : BindingFragment<WanAndroidAppFragmentBinding>() {

    override fun getLayoutRes(): Int = R.layout.wan_android_app_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}


@AutoService(HomeApp::class)
class WanAndroidApp : HomeApp("wanandroid", "玩安卓", R.drawable.wan_android_app_icon) {
    override fun newFragment(): BaseFragment = WanAndroidAppFragment()
}