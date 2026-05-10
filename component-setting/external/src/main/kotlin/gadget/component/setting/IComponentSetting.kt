package gadget.component.setting

import gadget.basic.tool.singleton

interface IComponentSetting {

    companion object : IComponentSetting by singleton() {
    }
}