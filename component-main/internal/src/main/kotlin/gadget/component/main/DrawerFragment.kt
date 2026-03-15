package gadget.component.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import gadget.basic.arch.GadgetFragment
import gadget.component.main.internal.databinding.DrawerFragmentBinding
import kotlin.math.absoluteValue

class DrawerFragment : GadgetFragment() {

    private val viewBinding: DrawerFragmentBinding by lazy(LazyThreadSafetyMode.NONE) {
        DrawerFragmentBinding.inflate(layoutInflater).also { binding ->
            binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percent = verticalOffset.absoluteValue.toFloat() / appBarLayout.totalScrollRange.toFloat()
                binding.userLayout.alpha = percent
                binding.photoLayout.alpha = 1F - percent
                binding.vBackgroundMask.alpha = 1F - percent
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = viewBinding.root

    fun attachTo(activity: ComponentMainActivity, parent: ViewGroup) {
        if (this.view == null) {
            val container = FrameLayout(activity).also { it.id = View.generateViewId() }
            parent.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            activity.supportFragmentManager.beginTransaction()
                .add(container.id, this, DrawerFragment::class.java.simpleName)
                .commitAllowingStateLoss()
        } else {
            val container = this.view!!.parent as ViewGroup
            if (container.parent !== parent) {
                (container.parent as ViewGroup).removeView(container)
                parent.addView(container)
            }
        }
    }
}