package gadget.basic.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import gadget.basic.logger.Logger

abstract class GadgetActivity : AppCompatActivity() {

    protected val label: String = "${javaClass.simpleName}(${hashCode()})"

    protected open lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Logger.d(label) { "onAttachedToWindow()" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i(label) { "onCreate($savedInstanceState)" }

        windowInsetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsControllerCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        applyConfiguration()
    }

    override fun onRestart() {
        super.onRestart()
        Logger.d(label) { "onRestart()" }
    }

    override fun onStart() {
        super.onStart()
        Logger.i(label) { "onStart()" }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.d(label) { "onRestoreInstanceState($savedInstanceState)" }
    }

    override fun onResume() {
        super.onResume()
        Logger.i(label) { "onResume()" }
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        Logger.d(label) { "onTopResumedActivityChanged($isTopResumedActivity)" }
    }

    override fun onPause() {
        super.onPause()
        Logger.i(label) { "onPause()" }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Logger.d(label) { "onSaveInstanceState(${outState})" }
    }

    override fun onStop() {
        super.onStop()
        Logger.i(label) { "onStop()" }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.i(label) { "onDestroy()" }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Logger.d(label) { "onDetachedFromWindow()" }
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