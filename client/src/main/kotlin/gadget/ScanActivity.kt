package gadget

import alyx.gadget.R
import android.os.Bundle
import gadget.basic.activity.GadgetActivity
import gadget.component.scan.ComponentScanContract

class ScanActivity : GadgetActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.setFragmentResultListener(
            ComponentScanContract.REQUEST_SCAN_CLOSED,
            this,
        ) { _, _ ->
            finish()
        }
        setContentView(R.layout.scan_activity)
    }
}