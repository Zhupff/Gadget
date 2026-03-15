package gadget.basic.ui.common

object SideGravity {
    const val N  = 0
    const val L  = 1
    const val T  = 2
    const val R  = 4
    const val B  = 8
    const val TL = 3
    const val TR = 6
    const val BR = 12
    const val BL = 9
    const val A  = 15

    fun check(value: Int): Boolean =
        value == N || value == L || value == T || value == R || value == B ||
        value == TL || value == TR || value == BR || value == BL || value == A
}