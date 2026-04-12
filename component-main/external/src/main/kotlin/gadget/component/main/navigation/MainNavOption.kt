package gadget.component.main.navigation

import gadget.basic.tool.iteration

abstract class MainNavOption(
    val id: OptionID,
    /** drawable res id */
    val icon: Int,
    /** string res id */
    val name: Int,
) {
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

    open fun isClickable(): Boolean = true

    open fun isSelectable(): Boolean = true

    open fun onClick() {}

    override fun toString(): String = id.name
}