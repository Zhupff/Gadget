package the.gadget.modulebase.skin

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import dalvik.system.DexClassLoader
import the.gadget.modulebase.application.ApplicationApi
import the.gadget.modulebase.livedata.ArrayListLiveData
import the.gadget.modulebase.logcat.logD
import the.gadget.modulebase.logcat.logW
import the.gadget.modulebase.resource.ResourceApi
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@AutoService(SkinApi::class)
class SkinApiImpl : SkinApi {

    private val allSkinResource: MutableList<SkinResource> = mutableListOf()
    private val allSkinPackage: ArrayListLiveData<SkinPackage> = ArrayListLiveData()
    private val defaultSkinPackage: SkinPackage
    private val selectedSkinPackage: AtomicReference<SkinPackage>
    private val selectedStateSkinPackage: MutableState<SkinPackage>

    private val applicationResources: Resources = ResourceApi.instance.getResources()
    private val packageName: String = ApplicationApi.instance.getPackageName()

    init {
        ServiceLoader.load(SkinResource::class.java).let { allSkinResource.addAll(it) }
        ServiceLoader.load(SkinPackage::class.java)
            .sortedBy { it.id }
            .onEach { it.initResources(applicationResources, null) }
            .let {
                defaultSkinPackage = it.first()
                selectedSkinPackage = AtomicReference(defaultSkinPackage)
                selectedStateSkinPackage = mutableStateOf(defaultSkinPackage)
                allSkinPackage.addAll(it).commit()
            }
    }


    override fun getAllSkinPackage(): LiveData<List<SkinPackage>> = allSkinPackage

    override fun getSelectedSkinPackage(): SkinPackage = selectedSkinPackage.get()

    @Composable
    override fun getSelectedStateSkinPackage(): SkinPackage = selectedStateSkinPackage.value

    override fun changeSkin(skinPackage: SkinPackage) {
        logD("changeSkin(${skinPackage})")
        if (selectedSkinPackage.get().id == skinPackage.id) return
        selectedSkinPackage.set(skinPackage)
        selectedStateSkinPackage.value = selectedSkinPackage.get()
        allSkinResource.forEach { it.notifyChange() }
        allSkinPackage.commit()
    }

    override fun loadSkinPackage(filePath: String, className: String): SkinPackage? {
        try {
            val file = File(filePath)
            if (file.exists()) {
                val dexClassLoader = DexClassLoader(file.absolutePath,
                    ApplicationApi.instance.getApplication().cacheDir.absolutePath, null,
                    ApplicationApi.instance.getApplication().classLoader)
                val cls = dexClassLoader.loadClass(className)
                val instance = cls.newInstance()
                if (instance is SkinPackage) {
                    instance.initResources(applicationResources, file.absolutePath)
                    return instance
                }
            }
        } catch (e: Exception) {
            logW(e)
        }
        return null
    }

    override fun addSkinPackage(skinPackage: SkinPackage) {
        if (allSkinPackage.value.find { it.id == skinPackage.id } == null) {
            allSkinPackage.add(skinPackage).value.sortBy { it.id }
            allSkinPackage.commit()
        }
    }

    override fun getIdentify(skinPackage: SkinPackage, id: Int): Int {
        if (skinPackage == defaultSkinPackage) {
            return id
        }
        return try {
            val name = defaultSkinPackage.resources.getResourceEntryName(id)
                .replaceFirst(defaultSkinPackage.prefix, skinPackage.prefix)
            val type = defaultSkinPackage.resources.getResourceTypeName(id)
            skinPackage.resources.getIdentifier(name, type, packageName)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override fun getColorInt(id: Int): Int {
        return getColorInt(selectedSkinPackage.get(), id)
    }

    override fun getColorInt(skinPackage: SkinPackage, id: Int): Int {
        val targetId = getIdentify(skinPackage, id)
        return if (targetId != 0) {
            skinPackage.resources.getColor(targetId)
        } else {
            defaultSkinPackage.resources.getColor(id)
        }
    }

    override fun getColorStateList(id: Int): ColorStateList? = getColorStateList(selectedSkinPackage.get(), id)

    override fun getColorStateList(skinPackage: SkinPackage, id: Int): ColorStateList? {
        val targetId = getIdentify(skinPackage, id)
        return try {
            if (targetId != 0) {
                skinPackage.resources.getColorStateList(targetId)
            } else {
                defaultSkinPackage.resources.getColorStateList(id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getDrawable(id: Int): Drawable? = getDrawable(selectedSkinPackage.get(), id)

    override fun getDrawable(skinPackage: SkinPackage, id: Int): Drawable? {
        val targetId = getIdentify(skinPackage, id)
        return try {
            if (targetId != 0) {
                skinPackage.resources.getDrawable(targetId)
            } else {
                defaultSkinPackage.resources.getDrawable(id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}