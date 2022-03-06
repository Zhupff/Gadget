package the.gadget.modulebase.skin

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import java.util.*

interface SkinApi {
    companion object {
        @JvmStatic
        val instance: SkinApi by lazy {
            ServiceLoader.load(SkinApi::class.java).first()
        }
    }

    fun getAllSkinPackage(): LiveData<List<SkinPackage>>

    fun getSelectedSkinPackage(): SkinPackage

    @Composable
    fun getSelectedStateSkinPackage(): SkinPackage

    @MainThread
    fun changeSkin(skinPackage: SkinPackage)

    @MainThread
    fun changeSkinRandomly()

    fun loadSkinPackage(filePath: String, className: String): SkinPackage?

    fun addSkinPackage(skinPackage: SkinPackage)


    fun getIdentify(skinPackage: SkinPackage, id: Int): Int

    fun getColorInt(id: Int): Int
    fun getColorInt(skinPackage: SkinPackage, id: Int): Int

    fun getColorStateList(id: Int): ColorStateList?
    fun getColorStateList(skinPackage: SkinPackage, id: Int): ColorStateList?

    fun getDrawable(id: Int): Drawable?
    fun getDrawable(skinPackage: SkinPackage, id: Int): Drawable?
}