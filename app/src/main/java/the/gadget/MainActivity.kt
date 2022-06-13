package the.gadget

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import the.gadget.activity.BaseActivity
import the.gadget.component.home.activity.HomeActivity
import the.gadget.theme.ThemeApi
import the.gadget.weight.wallpaper.WallpaperView

class MainActivity : BaseActivity() {

    private val wallpaperView: WallpaperView by lazy {
        WallpaperView(this).also {
            it.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(wallpaperView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        lifecycleScope.launch(Dispatchers.IO) {
            ThemeApi.instance.initTheme()
        }
        lifecycleScope.launch {
            delay(1000)
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            this@MainActivity.finish()
        }
    }
}