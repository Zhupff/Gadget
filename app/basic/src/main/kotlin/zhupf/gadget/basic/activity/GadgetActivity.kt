package zhupf.gadget.basic.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import zhupf.gadgets.basic.OnConfigurationChangedDispatcher
import zhupf.gadgets.basic.OnConfigurationChangedListener
import zhupf.gadgets.logger.logV
import java.util.concurrent.CopyOnWriteArraySet

abstract class GadgetActivity : AppCompatActivity(), OnConfigurationChangedDispatcher {

    protected open val TAG: String = "${this::class.java.simpleName}(${hashCode()})"

    protected val onConfigurationChangedListeners = CopyOnWriteArraySet<OnConfigurationChangedListener>()

    protected lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat
        private set

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        TAG.logV("onAttachedToWindow()")
        windowInsetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsControllerCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        TAG.logV("onRestoreInstanceState($savedInstanceState)")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG.logV("onCreate($savedInstanceState)")
    }

    override fun onRestart() {
        super.onRestart()
        TAG.logV("onRestart()")
    }

    override fun onStart() {
        super.onStart()
        TAG.logV("onStart()")
    }

    override fun onResume() {
        super.onResume()
        TAG.logV("onResume()")
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        TAG.logV("onTopResumedActivityChanged($isTopResumedActivity)")
    }

    override fun onPause() {
        super.onPause()
        TAG.logV("onPause()")
    }

    override fun onStop() {
        super.onStop()
        TAG.logV("onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        TAG.logV("onDestroy()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        TAG.logV("onSaveInstanceState(${outState})")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        TAG.logV("onDetachedFromWindow()")
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