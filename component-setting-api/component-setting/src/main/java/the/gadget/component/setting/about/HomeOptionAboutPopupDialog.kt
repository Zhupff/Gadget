package the.gadget.component.setting.about

import android.os.Bundle
import android.view.View
import the.gadget.GadgetApplication
import the.gadget.component.setting.R
import the.gadget.component.setting.databinding.HomeOptionAboutPopupDialogBinding
import the.gadget.fragment.BindingDialogFragment

class HomeOptionAboutPopupDialog : BindingDialogFragment<HomeOptionAboutPopupDialogBinding>() {

    override fun getLayoutRes(): Int = R.layout.home_option_about_popup_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { dismissAllowingStateLoss() }
        binding.tvVersion.text = "版本: ${GadgetApplication.APP_VERSION}"
        contentPopup(binding.content)
    }
}