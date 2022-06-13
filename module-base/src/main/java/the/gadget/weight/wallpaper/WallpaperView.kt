package the.gadget.weight.wallpaper

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import the.gadget.livedata.observeLifecycleOrForever
import the.gadget.theme.ThemeApi

class WallpaperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val wallpaperObserver = Observer<String> { wallpaper ->
        if (!wallpaper.isNullOrEmpty()) {
            val wallpaperBitmap = BitmapFactory.decodeFile(wallpaper)
            setImageBitmap(wallpaperBitmap)
        } else {
            setImageBitmap(null)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeApi.instance.getWallpaper().observeLifecycleOrForever(findViewTreeLifecycleOwner(), wallpaperObserver)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeApi.instance.getWallpaper().removeObserver(wallpaperObserver)
    }
}