package gadget.basic.udp

import gadget.basic.tool.singleton

interface UdpDiscover {

    companion object : UdpDiscover by singleton() {
    }

    fun start()

    fun stop()
}