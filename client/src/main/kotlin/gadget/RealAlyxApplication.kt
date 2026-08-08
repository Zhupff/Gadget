package gadget

import gadget.basic.network.UdpDiscover

class RealAlyxApplication : AlyxApplication() {

    override fun onCreate() {
        super.onCreate()
        UdpDiscover().start()
    }
}