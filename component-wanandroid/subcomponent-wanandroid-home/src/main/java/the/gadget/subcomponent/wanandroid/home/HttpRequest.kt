package the.gadget.subcomponent.wanandroid.home

import retrofit2.http.GET
import the.gadget.common.NetworkApi
import the.gadget.module.wanandroid.BASE_URL
import the.gadget.module.wanandroid.HttpResponse
import the.gadget.network.HttpInterface

@HttpInterface(BASE_URL)
internal interface HttpRequest {
    companion object {
        val instance: HttpRequest by lazy { NetworkApi.createHttpInterface(HttpRequest::class.java) }
    }

    @GET("banner/json")
    suspend fun requestBanner(): HttpResponse<Array<BannerModel>>
}