package gadget

import gadget.basic.network.udp.UdpDiscover

class RealAlyx : Alyx() {

    override fun onCreate() {
        super.onCreate()
        UdpDiscover().start()
    }
}