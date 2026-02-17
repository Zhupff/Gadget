package gadget.component.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import gadget.basic.arch.GadgetActivity
import gadget.basic.theme.ThemeInflateFactory
import gadget.component.main.internal.databinding.ComponentMainActivityBinding
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

class ComponentMainActivity : GadgetActivity() {

    private val viewBinding: ComponentMainActivityBinding by lazy(LazyThreadSafetyMode.NONE) {
        ComponentMainActivityBinding.inflate(layoutInflater)
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
                        }
                    }
                })
        }
    }
}