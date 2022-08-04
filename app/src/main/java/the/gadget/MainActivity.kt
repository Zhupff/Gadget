package the.gadget

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import the.gadget.activity.BaseActivity
import the.gadget.api.Api
import the.gadget.component.home.activity.HomeActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        Api.init()
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                delay(1000)
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                this@MainActivity.finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}