package the.gadget.modulebase.skin

import android.content.res.Resources
import android.util.SparseIntArray
import org.json.JSONObject

open class SkinPackage(val resources: Resources) {
    companion object {
        const val LIGHT_SKIN_ID = "light"
        const val DARK_SKIN_ID = "dark"
    }

    private val info: JSONObject =
        if (resources.assets.list("")?.contains("skin.json") == true) {
            JSONObject(resources.assets.open("skin.json").reader().readText())
        } else JSONObject()

    val id: String by lazy { info.optString("id", LIGHT_SKIN_ID) }

    val name: String by lazy { info.optString("name", "明亮模式") }

    val cache: SparseIntArray by lazy { SparseIntArray() }

    override fun toString(): String = info.toString()
}