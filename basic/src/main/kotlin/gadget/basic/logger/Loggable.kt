package gadget.basic.logger

interface Loggable {
    val loggable: String
}

fun Any.loggable(singleton: Boolean = false): String {
    return if (singleton) this::class.java.simpleName else "${this::class.java.simpleName}(${this.hashCode()})"
}
