package gadget.component.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import gadget.basic.arch.GadgetActivity
import gadget.component.main.internal.databinding.ComponentMainActivityBinding
import gadget.component.main.layout.ComponentMainLayout
import kotlinx.coroutines.delay
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
        splashScreen.setOnExitAnimationListener { splash ->
            lifecycleScope.launch {
                delay(500L)
                splash.remove()
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(ComponentMainLayout(this))
    }
}