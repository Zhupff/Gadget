package the.gadget.module.theme

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.auto.service.AutoService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import the.gadget.api.FileApi
import the.gadget.api.ResourceApi
import the.gadget.theme.Palette
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeView
import java.io.File

@AutoService(ThemeApi::class)
class ThemeApiImpl : ThemeApi {
    companion object {
        private val WALLPAPER_FILE: File?
            get() = FileApi.instance.getFile(FileApi.CACHE_DIR.resolve("wallpaper.png"))
    }

    private val currentTheme: MutableLiveData<Palette> = MutableLiveData()

    private val wallpaper: MutableLiveData<String> = MutableLiveData()

    override fun getCurrentTheme(): LiveData<Palette> = currentTheme

    override fun getWallpaper(): LiveData<String> = wallpaper

    override suspend fun initTheme() {
        if (currentTheme.value != null) return
        val wallpaperFile = WALLPAPER_FILE
        if (wallpaperFile != null && wallpaperFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(wallpaperFile.path)
            switchTheme(bitmap)
        } else {
            val color = ResourceApi.instance.getColorInt(R.color.themeOrigin)
            switchTheme(color)
        }
    }

    override suspend fun switchTheme(bitmap: Bitmap) {
        val mode = currentTheme.value?.mode ?: Palette.Mode.Light
        val newPalette = if (mode.isLightMode())
            getLightTheme(bitmap)
        else
            getDarkTheme(bitmap)
        val wallpaperFile = WALLPAPER_FILE
        val filePath = if (wallpaperFile != null) FileApi.instance.saveBitmap(bitmap, wallpaperFile).path else null
        MainScope().launch {
            wallpaper.postValue(filePath)
            switchTheme(newPalette)
        }
    }

    override suspend fun switchTheme(originArgb: Int) {
        if (currentTheme.value?.originArgb != originArgb) {
            val mode = currentTheme.value?.mode ?: Palette.Mode.Light
            val newPalette = if (mode.isLightMode())
                getLightTheme(originArgb)
            else
                getDarkTheme(originArgb)
            WALLPAPER_FILE?.let { wallpaperFile -> if (wallpaperFile.exists()) wallpaperFile.delete() }
            MainScope().launch {
                wallpaper.postValue(null)
                switchTheme(newPalette)
            }
        }
    }

    override suspend fun switchThemeMode() {
        val currentPalette = currentTheme.value ?: return
        val newPalette = if (currentPalette.mode.isLightMode())
            getDarkTheme(currentPalette.originArgb)
        else
            getLightTheme(currentPalette.originArgb)
        MainScope().launch {
            switchTheme(newPalette)
        }
    }

    @MainThread
    private fun switchTheme(palette: Palette) {
        if (currentTheme.value != palette) {
            palette.apply()
            currentTheme.value = palette
        }
    }

    override fun attachView(view: View): ThemeView = ThemeView.get(view) ?: ThemeViewImpl(view)

    override fun detachView(view: View) { ThemeView.get(view)?.release() }

    override suspend fun getLightTheme(bitmap: Bitmap): Palette {
        val pixel = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixel, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return getLightTheme(ColourQuantizer.quantize(pixel))
    }

    override suspend fun getDarkTheme(bitmap: Bitmap): Palette {
        val pixel = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixel, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return getDarkTheme(ColourQuantizer.quantize(pixel))
    }

    override suspend fun getLightTheme(argb: Int): Palette {
        val hct = ColourSolver.HCT(argb)
        return Palette(
            Palette.Mode.Light, argb,
            hct.a1.tone(40), hct.a1.tone(100),
            hct.a1.tone(90), hct.a1.tone(10),
            hct.a2.tone(40), hct.a2.tone(100),
            hct.a2.tone(90), hct.a2.tone(10),
            hct.a3.tone(40), hct.a3.tone(100),
            hct.a3.tone(90), hct.a3.tone(10),
            hct.error.tone(40), hct.error.tone(100),
            hct.error.tone(90), hct.error.tone(10),
            hct.n1.tone(99), hct.n1.tone(10),
            (0x33 shl 24) or (hct.n1.tone(99) and 0x00FFFFFF), hct.n1.tone(10),
            hct.n2.tone(90), hct.n2.tone(30),
            hct.n2.tone(50), hct.n1.tone(0),
            hct.n1.tone(20), hct.n1.tone(95), hct.a1.tone(80))
    }

    override suspend fun getDarkTheme(argb: Int): Palette {
        val hct = ColourSolver.HCT(argb)
        return Palette(
            Palette.Mode.Dark, argb,
            hct.a1.tone(80), hct.a1.tone(20),
            hct.a1.tone(30), hct.a1.tone(90),
            hct.a2.tone(80), hct.a2.tone(20),
            hct.a2.tone(30), hct.a2.tone(90),
            hct.a3.tone(80), hct.a3.tone(20),
            hct.a3.tone(30), hct.a3.tone(90),
            hct.error.tone(80), hct.error.tone(20),
            hct.error.tone(30), hct.error.tone(80),
            hct.n1.tone(10), hct.n1.tone(90),
            (0x33 shl 24) or (hct.n1.tone(10) and 0x00FFFFFF), hct.n1.tone(90),
            hct.n2.tone(30), hct.n2.tone(80),
            hct.n2.tone(60), hct.n1.tone(0),
            hct.n1.tone(90), hct.n1.tone(20), hct.a1.tone(40))
    }
}