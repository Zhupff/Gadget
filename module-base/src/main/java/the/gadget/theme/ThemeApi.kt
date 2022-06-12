package the.gadget.theme

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import the.gadget.api.apiInstance

interface ThemeApi {
    companion object {
        val instance: ThemeApi by lazy { apiInstance(ThemeApi::class.java) }
    }

    fun getCurrentTheme(): LiveData<Palette>

    suspend fun switchTheme(bitmap: Bitmap)

    suspend fun switchTheme(originArgb: Int)

    suspend fun switchTheme(palette: Palette)

    suspend fun switchThemeMode()

    @MainThread
    fun attachView(view: View): ThemeView

    @MainThread
    fun detachView(view: View)

    suspend fun getLightTheme(bitmap: Bitmap): Palette

    suspend fun getDarkTheme(bitmap: Bitmap): Palette

    suspend fun getLightTheme(argb: Int): Palette

    suspend fun getDarkTheme(argb: Int): Palette
}