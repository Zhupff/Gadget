package gadget.basic.link

import java.net.URLDecoder

/**
 * gadget://<biz>[:<ver>]?k1=v1&k2=v2
 */
data class GLink(
    /** 所属业务 */
    val biz: String,
    /** 版本 */
    val ver: Int,
    /** 参数 */
    val params: Map<String, String>
) {
    companion object {
        private const val SCHEME = "gadget://"

        fun parse(link: String): GLink? {
            if (!link.startsWith(SCHEME)) return null

            val content = link.removePrefix(SCHEME).split('?', limit = 2)
            val header = content.first().split(':', limit = 2)

            val biz = header[0].takeIf { it.isNotBlank() } ?: return null
            val rawVer = header.getOrNull(1)
            val ver = if (rawVer == null) {
                0
            } else {
                rawVer.toIntOrNull()?.takeIf { it >= 0 } ?: return null
            }
            val params = content.getOrNull(1)
                ?.takeIf { it.isNotEmpty() }
                ?.split('&')
                ?.associate { param ->
                    val entry = param.split('=', limit = 2)
                    if (entry.size != 2 || entry[0].isEmpty()) return null
                    val key = decode(entry[0]) ?: return null
                    val value = decode(entry[1]) ?: return null
                    key to value
                }
                .orEmpty()

            return GLink(biz, ver, params)
        }

        private fun decode(value: String): String? = try {
            URLDecoder.decode(value, Charsets.UTF_8.name())
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}