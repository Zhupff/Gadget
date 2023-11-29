package zhupf.gadget.module

import androidx.fragment.app.Fragment
import zhupf.gadget.common.OnConfigurationChangedDispatcher
import zhupf.gadget.common.OnConfigurationChangedListener
import java.util.concurrent.CopyOnWriteArraySet

abstract class GadgetFragment : Fragment(), OnConfigurationChangedDispatcher {

    protected open val onConfigurationChangedListeners: MutableCollection<OnConfigurationChangedListener> = CopyOnWriteArraySet()

    override fun addOnConfigurationChangedListener(listener: OnConfigurationChangedListener): Boolean {
        return onConfigurationChangedListeners.add(listener)
    }

    override fun removeOnConfigurationChangedListener(listener: OnConfigurationChangedListener): Boolean {
        return onConfigurationChangedListeners.remove(listener)
    }

    override fun clearOnConfigurationChangedListeners() {
        onConfigurationChangedListeners.clear()
    }
}