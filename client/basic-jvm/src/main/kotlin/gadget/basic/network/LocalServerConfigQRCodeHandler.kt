package gadget.basic.network

import com.google.auto.service.AutoService
import gadget.basic.kv.ProtoSerializer
import gadget.basic.logger.Logger
import gadget.basic.qrcode.QRCodeHandler
import gadget.basic.qrcode.QRCodeJson
import gadget.basic.tool.singleton
import kotlinx.coroutines.launch

@AutoService(QRCodeHandler::class)
internal class LocalServerConfigQRCodeHandler : QRCodeHandler {

    override fun handle(header: String): Boolean =
        header == QRCodeJson.HEADER_LOCAL_SERVER_CONFIG

    override fun handle(content: ByteArray, close: () -> Unit) {
        ProtoSerializer.ioScope.launch {
            runCatching {
                val config = LocalServerConfigProto.ADAPTER.decode(content)
                singleton<LocalServer.ILocalServerConfigDataStoreProvider>()
                    .provide()
                    .updateData { config }
            }.onSuccess {
                close()
            }.onFailure { throwable ->
                Logger.w("LocalServerConfigQRCodeHandler", throwable) {
                    "Failed to handle local server config QR code"
                }
                close()
            }
        }
    }
}
