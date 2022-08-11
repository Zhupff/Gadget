package the.gadget.module.theme

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import the.gadget.api.GlobalApi
import the.gadget.common.*
import the.gadget.theme.Scheme
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeView
import java.io.File

@GlobalApi(ThemeApi::class, lazy = false)
class ThemeApiImpl : ThemeApi {
    companion object {
        private val WALLPAPER_FILE: File; get() = FileApi.Static.WALLPAPER_DIR.resolve(ThemeApi.Static.WALLPAPER_FILE_NAME)
        private const val STORE_THEME_COLOR_KEY: String = "store_theme_color"
        private const val STORE_THEME_MODE_KEY: String = "store_theme_mode"
    }

    private val currentScheme: MutableLiveData<Scheme> = MutableLiveData()

    init {
        CoroutineScope(Dispatchers.IO).launch { initTheme() }
    }

    override fun getCurrentScheme(): LiveData<Scheme> = currentScheme

    override suspend fun initTheme() {
        if (currentScheme.value != null) return
        val wallpaperFile = WALLPAPER_FILE
        if (wallpaperFile.exists()) {
            val bitmap = ImageApi.loadWallpaperBitmap(wallpaperFile.path)
            switchTheme(bitmap)
        } else {
            val color = DataStoreApi.getGlobalInt(STORE_THEME_COLOR_KEY, ResourceApi.getColorInt(the.gadget.module.base.R.color.themeOrigin))
            switchTheme(color)
        }
    }

    override suspend fun switchTheme(bitmap: Bitmap) {
        val mode = currentScheme.value?.mode
            ?: if (DataStoreApi.getGlobalBoolean(STORE_THEME_MODE_KEY, true))
                Scheme.Mode.Light
            else
                Scheme.Mode.Dark
        val newScheme = createScheme(mode, bitmap)
        FileApi.saveBitmap(bitmap, WALLPAPER_FILE)
        MainScope().launch { switchScheme(newScheme) }
    }

    override suspend fun switchTheme(argb: Int) {
        val mode = currentScheme.value?.mode
            ?: if (DataStoreApi.getGlobalBoolean(STORE_THEME_MODE_KEY, true))
                Scheme.Mode.Light
            else
                Scheme.Mode.Dark
        val newScheme = createScheme(mode, argb, null)
        WALLPAPER_FILE.deleteIfExists()
        MainScope().launch { switchScheme(newScheme) }
    }

    override suspend fun switchMode() {
        val scheme = currentScheme.value ?: return
        val newScheme = createScheme(scheme.mode.reverse(), scheme.originArgb, scheme.wallpaper)
        MainScope().launch { switchScheme(newScheme) }
    }

    @MainThread
    private suspend fun switchScheme(scheme: Scheme) {
        if (currentScheme.value != scheme) {
            DataStoreApi.setGlobalInt(STORE_THEME_COLOR_KEY, scheme.originArgb)
            DataStoreApi.setGlobalBoolean(STORE_THEME_MODE_KEY, scheme.mode.isLightMode())
            currentScheme.value = scheme
        }
    }

    override fun attachView(view: View): ThemeView {
        val instance = view.getTag(the.gadget.module.base.R.id.theme_view_tag)
        if (instance != null) {
            if (instance is ThemeViewImpl)
                return instance
            else
                throw IllegalStateException("$view already bind $instance.")
        }
        return ThemeViewImpl(view)
    }

    override fun detachView(view: View) {
        (view.getTag(the.gadget.module.base.R.id.theme_view_tag) as? ThemeView)?.release()
    }

    private suspend fun createScheme(mode: Scheme.Mode, bitmap: Bitmap): Scheme {
        val pixel = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixel, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return createScheme(mode, ColourQuantizer.quantize(pixel), bitmap)
    }

    private suspend fun createScheme(mode: Scheme.Mode, argb: Int, bitmap: Bitmap?): Scheme {
        val hct = ColourSolver.HCT(argb)
        return if (mode.isLightMode())
            Scheme(Scheme.Mode.Light, argb, bitmap,
                hct.a1.tone(40), hct.a1.tone(100),
                hct.a1.tone(90), hct.a1.tone(10),
                hct.a2.tone(40), hct.a2.tone(100),
                hct.a2.tone(90), hct.a2.tone(10),
                hct.a3.tone(40), hct.a3.tone(100),
                hct.a3.tone(90), hct.a3.tone(10),
                hct.error.tone(40), hct.error.tone(100),
                hct.error.tone(90), hct.error.tone(10),
                hct.n1.tone(99), hct.n1.tone(10),
                hct.n1.tone(90), hct.n1.tone(30),
                (0x33 shl 24) or (hct.n1.tone(99) and 0x00FFFFFF),
                hct.n2.tone(50), hct.n1.tone(0),
                hct.a1.tone(80), hct.a2.tone(80), hct.a3.tone(80))
        else
            Scheme(Scheme.Mode.Dark, argb, bitmap,
                hct.a1.tone(80), hct.a1.tone(20),
                hct.a1.tone(30), hct.a1.tone(90),
                hct.a2.tone(80), hct.a2.tone(20),
                hct.a2.tone(30), hct.a2.tone(90),
                hct.a3.tone(80), hct.a3.tone(20),
                hct.a3.tone(30), hct.a3.tone(90),
                hct.error.tone(80), hct.error.tone(20),
                hct.error.tone(30), hct.error.tone(80),
                hct.n1.tone(10), hct.n1.tone(90),
                hct.n1.tone(30), hct.n1.tone(70),
                (0x33 shl 24) or (hct.n1.tone(10) and 0x00FFFFFF),
                hct.n2.tone(60), hct.n1.tone(0),
                hct.a1.tone(40), hct.a2.tone(40), hct.a3.tone(40))
    }
}