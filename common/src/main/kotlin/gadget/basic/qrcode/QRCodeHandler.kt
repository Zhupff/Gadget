package gadget.basic.qrcode

interface QRCodeHandler {

    fun handle(header: String): Boolean

    fun handle(content: ByteArray, close: () -> Unit)
}