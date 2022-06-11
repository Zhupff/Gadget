package the.gadget

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import the.gadget.activity.BaseActivity
import the.gadget.component.home.activity.HomeActivity
import the.gadget.theme.skinColor

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = skinColor(id = R.color.skin_background_color),
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                )
            }
        }
        lifecycleScope.launch {
            delay(1000)
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            this@MainActivity.finish()
        }
    }
}