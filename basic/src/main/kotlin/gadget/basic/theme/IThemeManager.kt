package gadget.basic.theme

import gadget.basic.tool.singleton

interface IThemeManager {

    companion object : IThemeManager by singleton() {
    }
}