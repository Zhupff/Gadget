package the.gadget.module.wanandroid

class HttpResponse<T> {

    var data: T? = null

    var errorCode: Int = 0

    var errorMsg: String? = null

    fun isSuccess(): Boolean = errorCode == 0
}