package zhupf.gadget.common

fun <T> List<T>.mutable(): MutableList<T> =
    if (this is MutableList) this else throw IllegalStateException("It is not a mutable object.")

fun <T> Set<T>.mutable(): MutableSet<T> =
    if (this is MutableSet) this else throw IllegalStateException("It is not a mutable object.")

fun <K, V> Map<K, V>.mutable(): MutableMap<K, V> =
    if (this is MutableMap) this else throw IllegalStateException("It is not a mutable object.")