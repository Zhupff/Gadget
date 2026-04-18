package gadget.component.main.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import gadget.basic.arch.GadgetFragment
import gadget.basic.ui.dsl.FrameLayout
import gadget.basic.ui.dsl.ImageView
import gadget.basic.ui.dsl.frameLayoutParams
import gadget.basic.ui.dsl.marginLayoutParams

class BackgroundFragment : GadgetFragment() {

    private lateinit var wallpaper: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FrameLayout(requireContext(), { marginLayoutParams() }) {
            wallpaper = ImageView({ frameLayoutParams(MATCH_PARENT, MATCH_PARENT) {
                gravity = Gravity.CENTER
            }})
        }
    }
}