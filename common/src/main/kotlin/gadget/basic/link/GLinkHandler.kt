package gadget.basic.link

import gadget.basic.tool.iteration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GLinkHandler {

    companion object : GLinkHandler {
        private val handlers = iteration<GLinkHandler>().associateBy { it.biz }

        override val biz: String = "default"

        suspend fun post(link: GLink) {
            withContext(Dispatchers.Main) {
                val handler = handlers[biz] ?: GLinkHandler
                handler.handle(link)
            }
        }

        override fun handle(link: GLink) {
        }
    }

    val biz: String

    fun handle(link: GLink)
}