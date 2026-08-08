package gadget

interface Alyx {

    companion object {
        private lateinit var instance: Alyx
    }

    val debuggable: Boolean

    fun init() {
        instance = this
    }
}