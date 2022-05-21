package the.gadget.modulebase.skin

import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.auto.service.AutoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.modulebase.application.ApplicationApi
import the.gadget.modulebase.common.FileApi
import the.gadget.modulebase.livedata.ArrayListLiveData
import the.gadget.modulebase.logcat.logE
import the.gadget.modulebase.logcat.logI
import the.gadget.modulebase.logcat.logW
import the.gadget.modulebase.resource.ResourceApi
import the.gadget.modulebase.R

@AutoService(SkinApi::class)
class SkinApiImpl : SkinApi {

    private val allSkinPackage: ArrayListLiveData<SkinPackage> = ArrayListLiveData()
    private val defaultSkinPackage: SkinPackage
    private val selectedSkinPackage: MutableLiveData<SkinPackage>
    private val selectedSkinPackageState: MutableState<SkinPackage>

    private val applicationResources: Resources by lazy { ResourceApi.instance.getResources() }
    private val packageName: String by lazy { ApplicationApi.instance.getPackageName() }

    init {
        defaultSkinPackage = SkinPackage(applicationResources)
        selectedSkinPackage = MutableLiveData(defaultSkinPackage)
        selectedSkinPackageState = mutableStateOf(defaultSkinPackage)
        allSkinPackage.add(defaultSkinPackage).commit()
        CoroutineScope(Dispatchers.IO).launch {
            applicationResources.assets.list("")
                ?.filter { it.endsWith(".skin") }
                ?.forEach { assetName ->
                    val assetInputStream = applicationResources.assets.open(assetName)
                    val cacheFile = FileApi.CACHE_DIR.resolve(assetName)
                    FileApi.instance.copy(assetInputStream, cacheFile)
                    loadSkinPackage(cacheFile.path)?.let { addSkinPackage(it) }
                }
        }
    }


    override fun getAllSkinPackage(): LiveData<List<SkinPackage>> = allSkinPackage

    override fun getSelectedSkinPackage(): SkinPackage = selectedSkinPackage.value!!

    override fun getSelectedSkinPackageLiveData(): LiveData<SkinPackage> = selectedSkinPackage

    @Composable
    override fun getSelectedSkinPackageState(): State<SkinPackage> = selectedSkinPackageState

    @MainThread
    override fun changeSkin(skinPackage: SkinPackage) {
        logI("changeSkin(${skinPackage})")
        if (selectedSkinPackage.value!!.id == skinPackage.id) return
        selectedSkinPackage.value = skinPackage
        selectedSkinPackageState.value = skinPackage
    }

    @MainThread
    override fun changeSkinRandomly() {
        if (allSkinPackage.size() <= 1) {
            logI("changeSkinRandomly failed cause there is only one skin-package.")
        } else {
            val skinPackage = allSkinPackage[(0 until allSkinPackage.size()).random()]
            if (selectedSkinPackage.value!!.id == skinPackage.id) {
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


    @MainThread
    override fun attachView(view: View): SkinView = SkinView.get(view) ?: SkinView(view)

    @MainThread
    override fun detachView(view: View) { (view.getTag(R.id.skin_view_tag) as? SkinView)?.release() }


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
            ResourcesCompat.ID_NULL
        }
    }

    override fun getColorInt(id: Int): Int {
        return getColorInt(selectedSkinPackage.value!!, id)
    }

    override fun getColorInt(skinPackage: SkinPackage, id: Int): Int {
        val targetId = getIdentify(skinPackage, id)
        return if (targetId != ResourcesCompat.ID_NULL) {
            ResourcesCompat.getColor(skinPackage.resources, targetId, null)
        } else {
            ResourcesCompat.getColor(defaultSkinPackage.resources, id, null)
        }
    }

    override fun getColorStateList(id: Int): ColorStateList? = getColorStateList(selectedSkinPackage.value!!, id)

    override fun getColorStateList(skinPackage: SkinPackage, id: Int): ColorStateList? {
        val targetId = getIdentify(skinPackage, id)
        return try {
            if (targetId != ResourcesCompat.ID_NULL) {
                ResourcesCompat.getColorStateList(skinPackage.resources, targetId, null)
            } else {
                ResourcesCompat.getColorStateList(defaultSkinPackage.resources, id, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getDrawable(id: Int): Drawable? = getDrawable(selectedSkinPackage.value!!, id)

    override fun getDrawable(skinPackage: SkinPackage, id: Int): Drawable? {
        val targetId = getIdentify(skinPackage, id)
        return try {
            if (targetId != ResourcesCompat.ID_NULL) {
                ResourcesCompat.getDrawable(skinPackage.resources, targetId, null)
            } else {
                ResourcesCompat.getDrawable(defaultSkinPackage.resources, id, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}