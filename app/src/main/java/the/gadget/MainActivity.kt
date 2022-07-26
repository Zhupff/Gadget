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
import kotlinx.coroutines.withContext
import the.gadget.activity.BaseActivity
import the.gadget.component.home.activity.HomeActivity
import the.gadget.component.user.ComponentUserApi
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
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        setContentView(wallpaperView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        lifecycleScope.launch(Dispatchers.IO) {
            ComponentUserApi.instance.login()
            ThemeApi.instance.initTheme()
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