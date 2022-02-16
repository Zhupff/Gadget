package the.gadget.modulebase.skinresource

import android.content.res.AssetManager
import android.content.res.Resources
import org.json.JSONObject
import the.gadget.modulebase.logcat.logW

abstract class SkinInfo {

    val id: Int by lazy { info.getInt("id") }

    val name: String by lazy { info.getString("name") }

    val prefix: String by lazy { info.getString("prefix") }

    private val info: JSONObject by lazy { initInfo() }

    lateinit var resources: Resources; private set

    protected open fun initInfo(): JSONObject = JSONObject()

    fun initResources(applicationResources: Resources, path: String?) {
        if (path.isNullOrEmpty()) {
            resources = applicationResources
        } else {
            try {
                val assetManager = AssetManager::class.java.newInstance()
                val addAssetPath = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
                addAssetPath.invoke(assetManager, path)
                resources = Resources(assetManager, applicationResources.displayMetrics, applicationResources.configuration)
            } catch (e: Exception) {
                logW(e)
                throw IllegalArgumentException("Cannot initResources with arguments(${applicationResources}, ${path}).")
            }
        }
    }

    override fun toString(): String = info.toString()
}