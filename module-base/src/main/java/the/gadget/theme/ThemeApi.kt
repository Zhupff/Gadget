package the.gadget.theme

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import the.gadget.api.FileApi
import the.gadget.api.apiInstance
import java.io.File

interface ThemeApi {
    companion object {
        val instance: ThemeApi by lazy { apiInstance(ThemeApi::class.java) }

        const val WALLPAPER_FILE_NAME: String = "wallpaper.png"

        val WALLPAPER_TEMP_FILE: File; get() = FileApi.WALLPAPER_TEMP_DIR.resolve(WALLPAPER_FILE_NAME)
    }

    fun getCurrentTheme(): LiveData<Palette>

    fun getWallpaper(): LiveData<String>

    suspend fun initTheme()

    suspend fun switchTheme(bitmap: Bitmap)

    suspend fun switchTheme(originArgb: Int)

    suspend fun switchThemeMode()

    @MainThread
    fun attachView(view: View): ThemeView

    @MainThread
    fun detachView(view: View)
}