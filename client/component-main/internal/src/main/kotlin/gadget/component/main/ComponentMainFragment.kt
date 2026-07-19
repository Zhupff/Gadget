package gadget.component.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gadget.basic.fragment.GadgetFragment
import gadget.basic.theme.GlobalTheme
import gadget.basic.theme.subscribeTheme
import gadget.basic.ui.dsl.FrameLayout
import gadget.basic.ui.dsl.marginLayoutParams

class ComponentMainFragment : GadgetFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FrameLayout(requireContext(), { marginLayoutParams {
            subscribeTheme(GlobalTheme.current) {
                setBackgroundColor(backgroundColor)
            }
        }}) {
        }
}