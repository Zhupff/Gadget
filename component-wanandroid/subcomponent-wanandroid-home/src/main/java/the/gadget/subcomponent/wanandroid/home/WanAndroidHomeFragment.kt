package the.gadget.subcomponent.wanandroid.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import the.gadget.common.logD
import the.gadget.fragment.BindingFragment
import the.gadget.subcomponent.wanandroid.home.databinding.WanAndroidHomeFragmentBinding

class WanAndroidHomeFragment : BindingFragment<WanAndroidHomeFragmentBinding>() {

    override fun getLayoutRes(): Int = R.layout.wan_android_home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val response = HttpRequest.instance.requestBanner()
            logD(response.data?.map { it.toJsonString() })
        }
    }
}