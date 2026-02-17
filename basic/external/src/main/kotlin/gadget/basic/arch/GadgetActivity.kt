package gadget.basic.arch

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import gadget.basic.log.Loggable
import gadget.basic.log.logD
import gadget.basic.log.logI

abstract class GadgetActivity : AppCompatActivity(), Loggable by Loggable.Tag() {

    protected open lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        logD { "onAttachedToWindow()" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI { "onCreate($savedInstanceState)" }

        windowInsetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsControllerCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        applyConfiguration()
    }

    override fun onRestart() {
        super.onRestart()
        logD { "onRestart()" }
    }

    override fun onStart() {
        super.onStart()
        logI { "onStart()" }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD { "onRestoreInstanceState($savedInstanceState)" }
    }

    override fun onResume() {
        super.onResume()
        logI { "onResume()" }
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        logD { "onTopResumedActivityChanged($isTopResumedActivity)" }
    }

    override fun onPause() {
        super.onPause()
        logI { "onPause()" }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD { "onSaveInstanceState(${outState})" }
    }

    override fun onStop() {
        super.onStop()
        logI { "onStop()" }
    }

    override fun onDestroy() {
        super.onDestroy()
        logI { "onDestroy()" }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        logD { "onDetachedFromWindow()" }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyConfiguration(newConfig)
    }

    protected open fun applyConfiguration(configuration: Configuration = resources.configuration) {
        if (configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            windowInsetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
        }
        windowInsetsControllerCompat.isAppearanceLightStatusBars = !configuration.isNightModeActive
    }
}