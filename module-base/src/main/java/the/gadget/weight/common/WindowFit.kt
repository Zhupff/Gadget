package the.gadget.weight.common

interface WindowFit {

    val fitStatusBar: Boolean

    val fitNavigationBar: Boolean

    val statusBarHeight: Int

    val navigationBarHeight: Int

    fun fitSystemBar(fitStatusBar: Boolean, fitNavigationBar: Boolean)
}