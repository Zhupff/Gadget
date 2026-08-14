package gadget

import alyx.gadget.R
import android.os.Bundle
import gadget.basic.activity.GadgetActivity

class ScanActivity : GadgetActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_activity)
    }
}