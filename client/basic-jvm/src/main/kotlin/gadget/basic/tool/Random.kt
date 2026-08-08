package gadget.basic.tool

import java.security.SecureRandom

fun SecureRandom.nextString(chars: String, length: Int): String {
    return buildString {
        repeat(length.coerceAtLeast(1)) {
            append(chars[nextInt(chars.length)])
        }
    }
}
