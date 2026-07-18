package gadget

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import gadget.basic.activity.GadgetActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : GadgetActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen.setOnExitAnimationListener { splash ->
            lifecycleScope.launch {
                delay(500L)
                splash.remove()
            }
        }
        super.onCreate(savedInstanceState)
    }
}