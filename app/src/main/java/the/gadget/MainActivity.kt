package the.gadget

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import the.gadget.modulebase.activity.BaseActivity
import the.gadget.modulebase.skin.*
import the.gadget.modulehome.ModuleHomeApi

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
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            ModuleHomeApi.instance?.toHomeActivity(this@MainActivity)
            this@MainActivity.finish()
        }
    }
}