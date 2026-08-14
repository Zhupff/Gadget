package gadget.basic.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import gadget.basic.activity.GadgetActivity
import gadget.basic.logger.Logger

abstract class GadgetFragment : Fragment() {

    protected val label: String = "${javaClass.simpleName}(${hashCode()})"

    @MainThread
    fun isAlive(): Boolean =
        viewLifecycleOwnerLiveData.value
            ?.lifecycle
            ?.currentState
            ?.isAtLeast(Lifecycle.State.INITIALIZED) == true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Logger.d(label) { "onAttach($context)" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(label) { "onCreate($savedInstanceState)" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d(label) { "onCreateView($inflater, $container, $savedInstanceState)" }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i(label) { "onViewCreated($view, $savedInstanceState)" }
    }

    override fun onStart() {
        super.onStart()
        Logger.i(label) { "onStart()" }
    }

    override fun onResume() {
        super.onResume()
        Logger.i(label) { "onResume()" }
    }

    override fun onPause() {
        super.onPause()
        Logger.i(label) { "onPause()" }
    }

    override fun onStop() {
        super.onStop()
        Logger.i(label) { "onStop()" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.i(label) { "onDestroyView()" }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(label) { "onDestroy()" }
    }

    override fun onDetach() {
        super.onDetach()
        Logger.d(label) { "onDetach()" }
    }

    fun requireGadgetActivity(): GadgetActivity {
        return requireActivity() as GadgetActivity
    }
}