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
import the.gadget.api.BitmapApi
import the.gadget.api.FileApi
import the.gadget.api.ResourceApi
import the.gadget.api.deleteIfExists
import the.gadget.theme.Palette
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeView
import java.io.File

@AutoService(ThemeApi::class)
class ThemeApiImpl : ThemeApi {
    companion object {
        private val WALLPAPER_FILE: File; get() = FileApi.WALLPAPER_DIR.resolve(ThemeApi.WALLPAPER_FILE_NAME)
    }

    private val currentTheme: MutableLiveData<Palette> = MutableLiveData()

    private val wallpaper: MutableLiveData<Bitmap> = MutableLiveData()

    override fun getCurrentTheme(): LiveData<Palette> = currentTheme

    override fun getWallpaper(): LiveData<Bitmap> = wallpaper

    override suspend fun initTheme() {
        if (currentTheme.value != null) return
        val wallpaperFile = WALLPAPER_FILE
        if (wallpaperFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(wallpaperFile.path)
            switchTheme(bitmap)
        } else {
            val color = ResourceApi.instance.getColorInt(R.color.themeOrigin)
            switchTheme(color)
        }
    }

    override suspend fun switchTheme(bitmap: Bitmap) {
        val mode = currentTheme.value?.mode ?: Palette.Mode.Light
        val targetBitmap = BitmapApi.instance.zoomOut(bitmap, 1980 * 1080)
        val newPalette = if (mode.isLightMode())
            getLightPalette(targetBitmap)
        else
            getDarkPalette(targetBitmap)
        FileApi.instance.saveBitmap(targetBitmap, WALLPAPER_FILE).path
        MainScope().launch {
            wallpaper.value = targetBitmap
            switchTheme(newPalette)
        }
    }

    override suspend fun switchTheme(originArgb: Int) {
        if (currentTheme.value?.originArgb != originArgb) {
            val mode = currentTheme.value?.mode ?: Palette.Mode.Light
            val newPalette = if (mode.isLightMode())
                getLightPalette(originArgb)
            else
                getDarkPalette(originArgb)
            WALLPAPER_FILE.deleteIfExists()
            MainScope().launch {
                wallpaper.postValue(null)
                switchTheme(newPalette)
            }
        }
    }

    override suspend fun switchThemeMode() {
        val currentPalette = currentTheme.value ?: return
        val newPalette = if (currentPalette.mode.isLightMode())
            getDarkPalette(currentPalette.originArgb)
        else
            getLightPalette(currentPalette.originArgb)
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

    private suspend fun getLightPalette(bitmap: Bitmap): Palette {
        val pixel = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixel, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return getLightPalette(ColourQuantizer.quantize(pixel))
    }

    private suspend fun getDarkPalette(bitmap: Bitmap): Palette {
        val pixel = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixel, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return getDarkPalette(ColourQuantizer.quantize(pixel))
    }

    private suspend fun getLightPalette(argb: Int): Palette {
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

    private suspend fun getDarkPalette(argb: Int): Palette {
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