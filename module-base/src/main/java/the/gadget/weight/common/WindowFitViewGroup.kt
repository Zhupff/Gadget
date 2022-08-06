package the.gadget.weight.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import the.gadget.module.base.R

class WindowFitViewGroup(
    private val view: View,
    context: Context,
    attrs: AttributeSet? = null
) {

    var fitStatusBar: Boolean = false;private set

    var fitNavigationBar: Boolean = false;private set

    var statusBarHeight: Int = 0;private set

    var navigationBarHeight: Int = 0;private set

    init {
        context.obtainStyledAttributes(attrs, R.styleable.WindowFitViewGroup).also {
            fitStatusBar = it.getBoolean(R.styleable.WindowFitViewGroup_fitStatusBar, false)
            fitNavigationBar = it.getBoolean(R.styleable.WindowFitViewGroup_fitNavigationBar, false)
        }.recycle()
        view.setOnApplyWindowInsetsListener { _, insets ->
            val compatInsets = ViewCompat.getRootWindowInsets(view)
                ?.getInsets(WindowInsetsCompat.Type.systemBars())
            statusBarHeight = compatInsets?.top ?: 0
            navigationBarHeight = compatInsets?.bottom ?: 0
            fitSystemBar(fitStatusBar, fitNavigationBar)
            insets
        }
    }

    fun fitSystemBar(fitStatusBar: Boolean, fitNavigationBar: Boolean) {
        this.fitStatusBar = fitStatusBar
        this.fitNavigationBar = fitNavigationBar
        val targetPaddingTop = if (fitStatusBar) statusBarHeight else 0
        val targetPaddingBottom = if (fitNavigationBar) navigationBarHeight else 0
        if (view.paddingTop != targetPaddingTop || view.paddingBottom != targetPaddingBottom) {
            view.setPadding(0, targetPaddingTop, 0, targetPaddingBottom)
        }
    }
}