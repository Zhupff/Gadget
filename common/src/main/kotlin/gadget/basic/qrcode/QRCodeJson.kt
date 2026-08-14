package gadget.basic.qrcode

data class QRCodeJson(
    val header: String,
    val content: String,
) {
    companion object {
        const val HEADER_LOCAL_SERVER_CONFIG = "LocalServerConfig"
    }
}