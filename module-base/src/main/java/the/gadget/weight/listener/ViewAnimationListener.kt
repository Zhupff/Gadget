package the.gadget.weight.listener

import android.view.animation.Animation

open class ViewAnimationListener : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {}
    override fun onAnimationEnd(animation: Animation?) {}
    override fun onAnimationRepeat(animation: Animation?) {}
}