package the.gadget.theme

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import the.gadget.api.globalApi
import the.gadget.common.FileApi
import java.io.File

interface ThemeApi {
    companion object : ThemeApi by ThemeApi::class.globalApi()

    object Static {

        const val WALLPAPER_FILE_NAME: String = "wallpaper.png"

        val WALLPAPER_TEMP_FILE: File; get() = FileApi.Static.WALLPAPER_TEMP_DIR.resolve(WALLPAPER_FILE_NAME)
    }

    fun getCurrentScheme(): LiveData<Scheme>

    suspend fun initTheme()

    suspend fun switchTheme(bitmap: Bitmap)

    suspend fun switchTheme(argb: Int)

    suspend fun switchMode()

    @MainThread
    fun attachView(view: View): ThemeView

    @MainThread
    fun detachView(view: View)
}