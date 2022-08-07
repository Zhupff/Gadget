package the.gadget.network

import com.google.gson.Gson
import java.io.Serializable

abstract class JsonModel : Serializable {
    companion object {
        val GSON: Gson by lazy { Gson() }
    }

    open fun toJsonString(): String = GSON.toJson(this)
}