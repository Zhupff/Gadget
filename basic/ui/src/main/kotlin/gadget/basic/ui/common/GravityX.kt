package gadget.basic.ui.common

import gadget.basic.Gadget
import gadget.basic.exception.throws

object GravityX {
    const val N  = 0 // 0000
    const val TL = 1 // 0001
    const val TR = 2 // 0010
    const val BR = 4 // 0100
    const val BL = 8 // 1000
    const val L = TL or BL // 1001
    const val T = TL or TR // 0011
    const val R = TR or BR // 0110
    const val B = BR or BL // 1100
    const val A = TL or TR or BR or BL // 1111

    fun check(value: Int, defaultValue: Int = N): Int {
        if (value == N || value == TL || value == TR || value == BR || value == BL ||
            value == L || value == T  || value == R  || value == B  || value == A) {
            return value
        }
        if (Gadget.debuggable) {
            IllegalArgumentException("Illegal gravity value: $value!").throws()
        }
        return defaultValue
    }
}