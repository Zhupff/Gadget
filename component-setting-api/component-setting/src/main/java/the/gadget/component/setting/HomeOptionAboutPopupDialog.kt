package the.gadget.component.setting

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import the.gadget.api.ApplicationApi
import the.gadget.component.setting.databinding.HomeOptionAboutPopupDialogBinding
import the.gadget.fragment.BindingDialogFragment
import the.gadget.weight.beVisible
import the.gadget.weight.listener.ViewAnimatorListener
import the.gadget.weight.postAutoRemove

class HomeOptionAboutPopupDialog : BindingDialogFragment<HomeOptionAboutPopupDialogBinding>() {

    override fun getLayoutRes(): Int = R.layout.home_option_about_popup_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { dismissAllowingStateLoss() }
        binding.tvVersion.text = "版本: ${ApplicationApi.instance.getVersion()}"
        binding.content.postAutoRemove {
            ObjectAnimator.ofFloat(binding.content, "translationY", binding.content.height.toFloat(), 0F)
                .also {
                    it.duration = 200
                    it.interpolator = DecelerateInterpolator()
                    it.addListener(object : ViewAnimatorListener() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            binding.content.beVisible()
                        }
                    })
                }.start()
        }
    }
}