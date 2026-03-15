package gadget.component.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gadget.basic.arch.GadgetFragment

class DrawerFragment : GadgetFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return View(requireContext()).also { v ->
            v.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            v.setBackgroundColor(0xFFFF0000.toInt())
        }
    }
}