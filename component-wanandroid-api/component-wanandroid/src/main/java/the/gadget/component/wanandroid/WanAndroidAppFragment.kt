package the.gadget.component.wanandroid

import com.google.auto.service.AutoService
import the.gadget.component.home.HomeApp
import the.gadget.component.wanandroid.databinding.WanAndroidAppFragmentBinding
import the.gadget.fragment.BaseFragment
import the.gadget.fragment.BindingFragment

class WanAndroidAppFragment : BindingFragment<WanAndroidAppFragmentBinding>() {

    override fun getLayoutRes(): Int = R.layout.wan_android_app_fragment
}


@AutoService(HomeApp::class)
class WanAndroidApp : HomeApp("wanandroid", "玩安卓", R.drawable.wan_android_app_icon) {
    override fun newFragment(): BaseFragment = WanAndroidAppFragment()
}