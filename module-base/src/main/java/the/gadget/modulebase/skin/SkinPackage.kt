package the.gadget.modulebase.skin

import android.content.res.Resources
import org.json.JSONObject

open class SkinPackage(val resources: Resources) {

    private val info: JSONObject =
        if (resources.assets.list("")?.contains("skin.json") == true) {
            JSONObject(resources.assets.open("skin.json").reader().readText())
        } else JSONObject()

    val id: String by lazy { info.optString("id", "default") }

    val name: String by lazy { info.optString("name", "默认") }

    override fun toString(): String = info.toString()
}