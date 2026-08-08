package gadget

interface IApp {

    companion object {
        private lateinit var instance: IApp
    }

    val debuggable: Boolean

    fun init() {
        instance = this
    }
}