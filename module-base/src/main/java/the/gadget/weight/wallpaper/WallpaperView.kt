package the.gadget.weight.wallpaper

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import the.gadget.theme.ThemeApi

class WallpaperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        ThemeApi.instance.attachView(this).wallpaper()
    }
}