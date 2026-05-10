package gadget.component.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import gadget.basic.arch.GadgetActivity
import gadget.component.main.fragment.BackgroundFragment
import gadget.component.main.layout.MainLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComponentMainActivity : GadgetActivity() {

    private val mainLayout: MainLayout by lazy { MainLayout(this) }

    private val backgroundFragment: BackgroundFragment by lazy { BackgroundFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen.setOnExitAnimationListener { splash ->
            lifecycleScope.launch {
                delay(500L)
                splash.remove()
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(mainLayout)
        supportFragmentManager.beginTransaction()
            .add(mainLayout.backgroundContainer.id, backgroundFragment)
            .commitAllowingStateLoss()
    }
}