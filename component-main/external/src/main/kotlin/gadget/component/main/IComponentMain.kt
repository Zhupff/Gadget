package gadget.component.main

import gadget.basic.tool.singleton

interface IComponentMain {

    companion object : IComponentMain by singleton() {
    }
}