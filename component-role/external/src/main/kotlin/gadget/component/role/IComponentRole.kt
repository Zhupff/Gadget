package gadget.component.role

import gadget.basic.tool.singleton

interface IComponentRole {

    companion object : IComponentRole by singleton() {
    }
}