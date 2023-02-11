package com.common.lib.network

import com.common.lib.bean.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


interface Api {

    @POST("api/app/upgrade")
    fun checkVersion(@Body map: HashMap<String, Any>): Observable<BasicResponse<VersionBean>>

    @GET("api/passport/captcha")
    fun getCaptcha(): Observable<BasicResponse<PicCodeBean>>

    @POST("api/passport/register")
    fun register(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<UserBean>>

    @POST("api/passport/sendEmail")
    fun sendEmail(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<Any>>

    @POST("api/passport/login")
    fun login(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<UserBean>>

    @POST("api/article.notice/fetch")
    fun noticeList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<NoticeBean>>>

    @POST("api/banner/list")
    fun bannerList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<BannerBean>>>

    @GET("api/user/poster")
    fun poster(): Observable<BasicResponse<PosterBean>>


    @POST("api/passport/reset")
    fun resetLoginPassword(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/user.security/change")
    fun changePassword(@Body map: HashMap<String, Any>): Observable<BasicResponse<UserBean>>

    @GET("api/user.assets/list")
    fun assetsList(): Observable<BasicResponse<ArrayList<AssetsBean>>>

    @GET("api/home/data")
    fun homeData(): Observable<BasicResponse<ArrayList<HomeDataBean>>>

    @POST("api/user.assets/transfer")
    fun transfer(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/user.assets/tips")
    fun transferInfo(@Body map: HashMap<String, Any>): Observable<BasicResponse<TransferFeeBean>>

    @POST("api/user.assets/logs")
    fun transferList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<TransferBean>>>

    @POST("api/user/referee")
    fun inviteDetail(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<InviteBean>>>

    @POST("api/user.income/fetch")
    fun incomeRecord(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<IncomeBean>>>

    @GET("api/user.income/overview")
    fun incomeOverview(): Observable<BasicResponse<InviteBean>>

    @GET("api/user.profile/get")
    fun userProfile(): Observable<BasicResponse<UserBean>>

    @GET("api/app/protocol")
    fun appProtocol(): Observable<BasicResponse<QuestionBean>>

    @GET("api/app/meta")
    fun appMeta(): Observable<BasicResponse<MetaBean>>

    @POST("api/goods/fetch")
    fun goodsList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<GoodsBean>>>

    @POST("api/goods/info")
    fun goodsDetail(@Body map: HashMap<String, Any>): Observable<BasicResponse<GoodsBean>>


    @POST("api/order/submit")
    fun submitOrder(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/order/logistics")
    fun orderLogistics(@Body map: HashMap<String, Any>): Observable<BasicResponse<OrderBean>>

    @POST("api/order/fetch")
    fun orderList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<OrderBean>>>

    @POST("api/order/complete")
    fun orderComplete(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>


    @POST("api/user.address/fetch")
    fun addressList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<ReceiveAddressBean>>>


    @POST("api/district/list")
    fun districtList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<DistrictBean>>>


    @POST("api/user.address/add")
    fun addAddress(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/user.address/save")
    fun saveAddress(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/user.profile/save")
    fun saveProfile(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>


    @POST("api/user.address/destroy")
    fun deleteAddress(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>


    @POST("api/user.assets/exchange_info")
    fun assetsExchangeInfo(@Body map: HashMap<String, Any>): Observable<BasicResponse<ExchangeTipsBean>>

    @GET("api/user.assets/ticker")
    fun ticker(): Observable<BasicResponse<ArrayList<QuotationsBean>>>

    @POST("api/user.assets/exchange")
    fun exchange(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @GET("api/agent/goods")
    fun agentGoods(): Observable<BasicResponse<ArrayList<AgentBean>>>

    @POST("api/agent/submit")
    fun agentSubmit(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @GET("api/user.checkIn/overview")
    fun checkInOverview(): Observable<BasicResponse<CheckInBean>>

    @POST("api/user.checkIn/submit")
    fun checkInSubmit(): Observable<BasicResponse<Any>>


    @GET("api/article/contactUs")
    fun contactUs(): Observable<BasicResponse<NoticeBean>>
}