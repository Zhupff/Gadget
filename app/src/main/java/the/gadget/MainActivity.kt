package the.gadget

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import the.gadget.modulebase.activity.BaseActivity
import the.gadget.modulebase.skin.*
import the.gadget.modulebase.thread.ThreadApi
import the.gadget.modulehome.ModuleHomeApi

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = skinColor(id = SR.color.background_color_l1),
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        SkinApi.instance.changeSkinRandomly()
                    }
                )
            }
        }

        ThreadApi.instance.runOnMainThreadDelay(1000) {
            ModuleHomeApi.instance?.toHomeActivity(this)
        }
    }
}