package gadget.basic.network.udp

import gadget.basic.tool.singleton

interface UdpDiscover {

    companion object : UdpDiscover by singleton() {
    }

    fun start()

    fun stop()
}