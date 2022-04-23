package the.gadget.modulebase.skin

import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import the.gadget.modulebase.application.ApplicationApi
import the.gadget.modulebase.common.FileApi
import the.gadget.modulebase.livedata.ArrayListLiveData
import the.gadget.modulebase.logcat.logE
import the.gadget.modulebase.logcat.logI
import the.gadget.modulebase.logcat.logW
import the.gadget.modulebase.resource.ResourceApi
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@AutoService(SkinApi::class)
class SkinApiImpl : SkinApi {

    private val allSkinResource: MutableList<SkinResource> = mutableListOf()
    private val allSkinPackage: ArrayListLiveData<SkinPackage> = ArrayListLiveData()
    private val defaultSkinPackage: SkinPackage
    private val selectedSkinPackage: AtomicReference<SkinPackage>
    private val selectedStateSkinPackage: MutableState<SkinPackage>

    private val applicationResources: Resources by lazy { ResourceApi.instance.getResources() }
    private val packageName: String by lazy { ApplicationApi.instance.getPackageName() }

    init {
        ServiceLoader.load(SkinResource::class.java, ApplicationApi.instance.getClassLoader())
            .let { allSkinResource.addAll(it) }
        defaultSkinPackage = SkinPackage(applicationResources)
        selectedSkinPackage = AtomicReference(defaultSkinPackage)
        selectedStateSkinPackage = mutableStateOf(defaultSkinPackage)
        allSkinPackage.add(defaultSkinPackage).commit()
        applicationResources.assets.list("")
            ?.filter { it.endsWith(".skin") }
            ?.forEach { assetName ->
                val assetInputStream = applicationResources.assets.open(assetName)
                val cacheFile = FileApi.CACHE_DIR.resolve(assetName)
                FileApi.instance.copy(assetInputStream, cacheFile)
                loadSkinPackage(cacheFile.path)?.let { addSkinPackage(it) }
            }
    }


    override fun getAllSkinPackage(): LiveData<List<SkinPackage>> = allSkinPackage

    override fun getSelectedSkinPackage(): SkinPackage = selectedSkinPackage.get()

    @Composable
    override fun getSelectedStateSkinPackage(): SkinPackage = selectedStateSkinPackage.value

    @MainThread
    override fun changeSkin(skinPackage: SkinPackage) {
        logI("changeSkin(${skinPackage})")
        if (selectedSkinPackage.get().id == skinPackage.id) return
        selectedSkinPackage.set(skinPackage)
        selectedStateSkinPackage.value = selectedSkinPackage.get()
        allSkinResource.forEach { it.notifyChange() }
        allSkinPackage.commit()
    }

    @MainThread
    override fun changeSkinRandomly() {
        if (allSkinPackage.size() <= 1) {
            logI("changeSkinRandomly failed cause there is only one skin-package.")
        } else {
            val skinPackage = allSkinPackage[(0 until allSkinPackage.size()).random()]
            if (selectedSkinPackage.get().id == skinPackage.id) {
                changeSkinRandomly()
            } else {
                logI("changeSkinRandomly(${skinPackage})")
                changeSkin(skinPackage)
            }
        }
    }

    override fun loadSkinPackage(filePath: String): SkinPackage? {
        logI("loadSkinPackage(${filePath})")
        try {
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, filePath)
            val resources = Resources(assetManager, applicationResources.displayMetrics, applicationResources.configuration)
            return SkinPackage(resources)
        } catch (e: Exception) {
            logW("loadSkinPackage(${filePath}) failed.").logE(e)
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
            val type = defaultSkinPackage.resources.getResourceTypeName(id)
            skinPackage.resources.getIdentifier(name, type, packageName)
        } catch (e: Exception) {
            logW("getIdentity(${skinPackage}, ${id}) failed").logE(e)
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