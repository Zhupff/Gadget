package the.gadget

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import the.gadget.activity.BaseActivity
import the.gadget.component.home.activity.HomeActivity
import the.gadget.theme.ThemeApi

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            ThemeApi.instance.switchTheme(getColor(R.color.themeOrigin))
        }
        lifecycleScope.launch {
            delay(1000)
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            this@MainActivity.finish()
        }
    }
}