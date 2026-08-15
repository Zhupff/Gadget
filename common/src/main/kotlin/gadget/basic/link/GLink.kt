package gadget.basic.link

/**
 * gadget://<biz>:ver?k1=v1&k2=v2
 */
data class GLink(
    val biz: String,
    val ver: Int,
    val params: Map<String, String>
) {
    companion object {
//        fun parse(link: String): GLink {
//
//        }
    }
}