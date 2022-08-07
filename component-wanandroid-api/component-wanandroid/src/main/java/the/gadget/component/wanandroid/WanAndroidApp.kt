package the.gadget.component.wanandroid

import com.google.auto.service.AutoService
import the.gadget.component.home.HomeApp
import the.gadget.fragment.BaseFragment
import the.gadget.subcomponent.wanandroid.home.WanAndroidHomeFragment

@AutoService(HomeApp::class)
class WanAndroidApp : HomeApp("wanandroid", "玩安卓", R.drawable.wan_android_app_icon) {
    override fun newFragment(): BaseFragment = WanAndroidHomeFragment()
}