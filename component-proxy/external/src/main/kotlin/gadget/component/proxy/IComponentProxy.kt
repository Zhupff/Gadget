package gadget.component.proxy

import gadget.basic.tool.singleton

interface IComponentProxy {

    companion object : IComponentProxy by singleton() {
    }
}