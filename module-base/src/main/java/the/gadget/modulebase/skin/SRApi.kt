package the.gadget.modulebase.skin

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import java.util.*

interface SRApi {
    companion object {
        @JvmStatic
        val instance: SRApi by lazy {
            ServiceLoader.load(SRApi::class.java).first()
        }
    }

    fun getAllSkinInfo(): LiveData<List<SkinInfo>>

    fun getSelectedSkinInfo(): SkinInfo

    @Composable
    fun getSelectedStateSkinInfo(): SkinInfo

    @MainThread
    fun changeSkin(info: SkinInfo)

    fun loadSkinInfo(filePath: String, className: String): SkinInfo?

    fun addSkinInfo(info: SkinInfo)


    fun getIdentify(info: SkinInfo, id: Int): Int

    fun getColorInt(id: Int): Int
    fun getColorInt(info: SkinInfo, id: Int): Int

    fun getColorStateList(id: Int): ColorStateList?
    fun getColorStateList(info: SkinInfo, id: Int): ColorStateList?

    fun getDrawable(id: Int): Drawable?
    fun getDrawable(info: SkinInfo, id: Int): Drawable?
}