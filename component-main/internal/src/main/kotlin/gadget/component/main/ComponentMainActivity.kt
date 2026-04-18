package gadget.component.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import gadget.basic.arch.GadgetActivity
import gadget.component.main.fragment.BackgroundFragment
import gadget.component.main.layout.ComponentMainLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComponentMainActivity : GadgetActivity() {

    private val componentMainLayout: ComponentMainLayout by lazy { ComponentMainLayout(this) }

    private val backgroundFragment: BackgroundFragment by lazy { BackgroundFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen.setOnExitAnimationListener { splash ->
            lifecycleScope.launch {
                delay(500L)
                splash.remove()
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(componentMainLayout)
        supportFragmentManager.beginTransaction()
            .add(componentMainLayout.backgroundContainer.id, backgroundFragment)
            .commitAllowingStateLoss()
    }
}