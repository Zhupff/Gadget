package the.gadget.modulebase.skinresource

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

@AutoService(SRApi::class)
class SRApiImpl : SRApi {

    private val allSkinResource: MutableList<SkinResource> = mutableListOf()
    private val allSkinInfo: ArrayListLiveData<SkinInfo> = ArrayListLiveData()
    private val defaultSkinInfo: SkinInfo
    private val selectedSkinInfo: AtomicReference<SkinInfo>
    private val selectedStateSkinInfo: MutableState<SkinInfo>

    private val applicationResources: Resources = ResourceApi.instance.getResources()
    private val packageName: String = ApplicationApi.instance.getPackageName()

    init {
        ServiceLoader.load(SkinResource::class.java).let { allSkinResource.addAll(it) }
        ServiceLoader.load(SkinInfo::class.java)
            .sortedBy { it.id }
            .onEach { it.initResources(applicationResources, null) }
            .let {
                defaultSkinInfo = it.first()
                selectedSkinInfo = AtomicReference(defaultSkinInfo)
                selectedStateSkinInfo = mutableStateOf(defaultSkinInfo)
                allSkinInfo.addAll(it).commit()
            }
    }


    override fun getAllSkinInfo(): LiveData<List<SkinInfo>> = allSkinInfo

    override fun getSelectedSkinInfo(): SkinInfo = selectedSkinInfo.get()

    @Composable
    override fun getSelectedStateSkinInfo(): SkinInfo = selectedStateSkinInfo.value

    override fun changeSkin(info: SkinInfo) {
        logD("changeSkin(${info})")
        if (selectedSkinInfo.get().id == info.id) return
        selectedSkinInfo.set(info)
        selectedStateSkinInfo.value = selectedSkinInfo.get()
        allSkinResource.forEach { it.notifyChange() }
        allSkinInfo.commit()
    }

    override fun loadSkinInfo(filePath: String, className: String): SkinInfo? {
        try {
            val file = File(filePath)
            if (file.exists()) {
                val dexClassLoader = DexClassLoader(file.absolutePath,
                    ApplicationApi.instance.getApplication().cacheDir.absolutePath, null,
                    ApplicationApi.instance.getApplication().classLoader)
                val cls = dexClassLoader.loadClass(className)
                val instance = cls.newInstance()
                if (instance is SkinInfo) {
                    instance.initResources(applicationResources, file.absolutePath)
                    return instance
                }
            }
        } catch (e: Exception) {
            logW(e)
        }
        return null
    }

    override fun addSkinInfo(info: SkinInfo) {
        if (allSkinInfo.value.find { it.id == info.id } == null) {
            allSkinInfo.add(info).value.sortBy { it.id }
            allSkinInfo.commit()
        }
    }

    override fun getIdentify(info: SkinInfo, id: Int): Int {
        if (info == defaultSkinInfo) {
            return id
        }
        return try {
            val name = defaultSkinInfo.resources.getResourceEntryName(id).replaceFirst(defaultSkinInfo.prefix, info.prefix)
            val type = defaultSkinInfo.resources.getResourceTypeName(id)
            info.resources.getIdentifier(name, type, packageName)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override fun getColorInt(id: Int): Int {
        return getColorInt(selectedSkinInfo.get(), id)
    }

    override fun getColorInt(info: SkinInfo, id: Int): Int {
        val targetId = getIdentify(info, id)
        return if (targetId != 0) {
            info.resources.getColor(targetId)
        } else {
            defaultSkinInfo.resources.getColor(id)
        }
    }

    override fun getColorStateList(id: Int): ColorStateList? = getColorStateList(selectedSkinInfo.get(), id)

    override fun getColorStateList(info: SkinInfo, id: Int): ColorStateList? {
        val targetId = getIdentify(info, id)
        return try {
            if (targetId != 0) {
                info.resources.getColorStateList(targetId)
            } else {
                defaultSkinInfo.resources.getColorStateList(id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getDrawable(id: Int): Drawable? = getDrawable(selectedSkinInfo.get(), id)

    override fun getDrawable(info: SkinInfo, id: Int): Drawable? {
        val targetId = getIdentify(info, id)
        return try {
            if (targetId != 0) {
                info.resources.getDrawable(targetId)
            } else {
                defaultSkinInfo.resources.getDrawable(id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}