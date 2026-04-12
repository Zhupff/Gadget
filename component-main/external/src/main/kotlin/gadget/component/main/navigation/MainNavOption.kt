package gadget.component.main.navigation

import gadget.basic.tool.iteration

interface MainNavOption {
    companion object {
        val ALL: List<MainNavOption> = iteration<MainNavOption>().sortedBy { it.id }
    }

    enum class OptionID {
        HOME,
        VIDEO,
        AUDIO,
        ROLE,
        SETTING,
        ABOUT,
        ;
    }

    val id: OptionID
    /** drawable res id */
    val icon: Int
    /** string res id */
    val name: Int

    fun isClickable(): Boolean = true

    fun isSelectable(): Boolean = true

    fun onClick() {}
}