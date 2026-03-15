package gadget.component.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}