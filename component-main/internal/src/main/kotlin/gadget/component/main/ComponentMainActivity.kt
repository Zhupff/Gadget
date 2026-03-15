package gadget.component.main

import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import gadget.basic.arch.GadgetActivity
import gadget.basic.theme.ThemeInflateFactory
import gadget.component.main.internal.R
import gadget.component.main.internal.databinding.ComponentMainActivityBinding
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

class ComponentMainActivity : GadgetActivity() {

    private val viewBinding: ComponentMainActivityBinding by lazy(LazyThreadSafetyMode.NONE) {
        ComponentMainActivityBinding.inflate(layoutInflater).also { binding ->
            binding.root.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                viewBinding.mainContainer.closeDrawer(viewBinding.mainDrawer)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 = ThemeInflateFactory(layoutInflater.factory2)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        lifecycleScope.launch {
            WindowInfoTracker.getOrCreate(this@ComponentMainActivity)
                .windowLayoutInfo(this@ComponentMainActivity)
                .collect(object : FlowCollector<WindowLayoutInfo> {
                    private var once = false
                    private var fold = false
                    override suspend fun emit(value: WindowLayoutInfo) {
                        val flat = value.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
                            ?.let { it.state == FoldingFeature.State.FLAT || it.state == FoldingFeature.State.HALF_OPENED }
                            ?: false
                        if (!once || fold == flat) {
                            once = true
                            fold = !flat
                            onWindowChanged(fold)
                        }
                    }
                })
        }
    }

    private fun onWindowChanged(folding: Boolean) {
        val drawerFragment = supportFragmentManager.findFragmentByTag(DrawerFragment::class.java.simpleName)
        val drawerFragmentTag = DrawerFragment::class.java.simpleName
        if (folding) {
            if (drawerFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(drawerFragment)
                    .commitNow()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainDrawer, drawerFragment, drawerFragmentTag)
                    .commitAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainDrawer, DrawerFragment(), drawerFragmentTag)
                    .commitAllowingStateLoss()
            }
            viewBinding.mainContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, viewBinding.mainDrawer)
            viewBinding.sideContainer.isGone = true
        } else {
            if (drawerFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(drawerFragment)
                    .commitNow()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.sideDrawer, drawerFragment, drawerFragmentTag)
                    .commitAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.sideDrawer, DrawerFragment(), drawerFragmentTag)
                    .commitAllowingStateLoss()
            }
            viewBinding.mainContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, viewBinding.mainDrawer)
            viewBinding.sideContainer.isVisible = true
        }
    }
}