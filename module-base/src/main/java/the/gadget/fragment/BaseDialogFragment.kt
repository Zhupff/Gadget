package the.gadget.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.HookDialogFragment
import the.gadget.module.base.R
import the.gadget.weight.beInvisible
import the.gadget.weight.beVisible
import the.gadget.weight.listener.ViewAnimatorListener
import the.gadget.weight.postAutoRemove

abstract class BaseDialogFragment : HookDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext(), R.style.BaseDialogFragmentTheme)

    protected open fun contentPopup(view: View) {
        view.beInvisible()
        view.postAutoRemove {
            ObjectAnimator.ofFloat(view, "translationY", view.height.toFloat(), 0F)
                .also {
                    it.duration = 200
                    it.interpolator = DecelerateInterpolator()
                    it.addListener(object : ViewAnimatorListener() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            view.beVisible()
                        }
                    })
                }.start()
        }
    }
}