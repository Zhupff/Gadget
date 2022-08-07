package the.gadget.subcomponent.wanandroid.home

import the.gadget.network.JsonModel


internal class BannerModel : JsonModel() {

    var id: Int = 0

    var title: String? = null

    var desc: String? = null

    var url: String? = null

    var imagePath: String? = null

    var isVisible: Int = 1

    fun isValid(): Boolean = isVisible == 1 && !imagePath.isNullOrEmpty()
}