package gadget

import gadget.basic.network.UdpDiscover

class RealApp : App() {

    override fun onCreate() {
        super.onCreate()
        UdpDiscover().start()
    }
}