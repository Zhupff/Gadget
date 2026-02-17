package gadget.basic.arch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gadget.basic.log.Loggable
import gadget.basic.log.logD
import gadget.basic.log.logI

abstract class GadgetFragment : Fragment(), Loggable by Loggable.Tag() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logD { "onAttach($context)" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD { "onCreate($savedInstanceState)" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logD { "onCreateView($inflater, $container, $savedInstanceState)" }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI { "onViewCreated($view, $savedInstanceState)" }
    }

    override fun onStart() {
        super.onStart()
        logI { "onStart()" }
    }

    override fun onResume() {
        super.onResume()
        logI { "onResume()" }
    }

    override fun onPause() {
        super.onPause()
        logI { "onPause()" }
    }

    override fun onStop() {
        super.onStop()
        logI { "onStop()" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logI { "onDestroyView()" }
    }

    override fun onDestroy() {
        super.onDestroy()
        logD { "onDestroy()" }
    }

    override fun onDetach() {
        super.onDetach()
        logD { "onDetach()" }
    }

    fun requireGadgetActivity(): GadgetActivity {
        return requireActivity() as GadgetActivity
    }
}