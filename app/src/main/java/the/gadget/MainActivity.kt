package the.gadget

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import the.gadget.modulebase.activity.BaseActivity
import the.gadget.modulebase.skin.SkinApi
import the.gadget.modulebase.skin.srColor
import the.gadget.modulebase.skin.srDrawablePainter

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = srColor(id = SR.color.background_color_l1),
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = srDrawablePainter(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        SkinApi.instance.changeSkinRandomly()
                    }
                )
            }
        }
    }
}