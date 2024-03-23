package zhupf.gadget.basic.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import zhupf.gadgets.basic.OnConfigurationChangedDispatcher
import zhupf.gadgets.basic.OnConfigurationChangedListener
import zhupf.gadgets.logger.logV
import java.util.concurrent.CopyOnWriteArraySet

abstract class GadgetFragment : Fragment(), OnConfigurationChangedDispatcher {

    protected open val TAG: String = "${this::class.java.simpleName}(${hashCode()})"

    protected lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat
        private set

    protected val onConfigurationChangedListeners = CopyOnWriteArraySet<OnConfigurationChangedListener>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TAG.logV("onAttach($context)")
        windowInsetsControllerCompat = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG.logV("onCreate($savedInstanceState)")
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TAG.logV("onCreateView($inflater, $container, $savedInstanceState)")
        return createView(inflater, container, savedInstanceState) ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TAG.logV("onViewCreated($view, $savedInstanceState)")
    }

    override fun onStart() {
        super.onStart()
        TAG.logV("onStart()")
    }

    override fun onResume() {
        super.onResume()
        TAG.logV("onResume()")
    }

    override fun onPause() {
        super.onPause()
        TAG.logV("onPause()")
    }

    override fun onStop() {
        super.onStop()
        TAG.logV("onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        TAG.logV("onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        TAG.logV("onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        TAG.logV("onDetach()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        TAG.logV("onConfigurationChanged($newConfig)")
        onConfigurationChangedListeners.forEach { it.onConfigurationChanged(newConfig) }
    }

    override fun addOnConfigurationChangedListener(listener: OnConfigurationChangedListener): Boolean =
        onConfigurationChangedListeners.add(listener)

    override fun removeOnConfigurationChangedListener(listener: OnConfigurationChangedListener): Boolean =
        onConfigurationChangedListeners.remove(listener)

    override fun clearOnConfigurationChangedListeners() {
        onConfigurationChangedListeners.clear()
    }
}