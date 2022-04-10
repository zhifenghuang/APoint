package com.common.lib.network

import com.common.lib.bean.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


interface Api {

    @POST("api/app/upgrade")
    fun checkVersion(@Body map: HashMap<String, Any>): Observable<BasicResponse<VersionBean>>

    @GET("api/passport/captcha")
    fun getCaptcha(): Observable<BasicResponse<PicCodeBean>>

    @POST("api/passport/register")
    fun register1(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<UserBean>>

    @POST("api/passport/sendEmail")
    fun sendEmail(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<Any>>

    @POST("api/passport/complete")
    fun register2(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<UserBean>>

    @POST("api/passport/login")
    fun login(
        @Body map: HashMap<String, Any>
    ): Observable<BasicResponse<UserBean>>

    @POST("api/article.notice/read")
    fun readNotice(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/article.notice/get")
    fun noticeDetail(@Body map: HashMap<String, Any>): Observable<BasicResponse<NoticeBean>>

    @POST("api/article.notice/fetch")
    fun noticeList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<NoticeBean>>>

    @POST("api/banner/list")
    fun bannerList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<BannerBean>>>

    @GET("api/article/question")
    fun questionList(): Observable<BasicResponse<ArrayList<QuestionBean>>>

    @GET("api/article/rule")
    fun awardRule(): Observable<BasicResponse<QuestionBean>>

    @GET("api/article/help")
    fun serviceHelp(): Observable<BasicResponse<QuestionBean>>

    @GET("api/article/aboutUs")
    fun aboutUs(): Observable<BasicResponse<QuestionBean>>

    @POST("api/article/guide")
    fun faq(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<QuestionBean>>>

    @GET("api/user/poster")
    fun poster(): Observable<BasicResponse<PosterBean>>

    @GET("api/user.security/google")
    fun getGoogleCode(): Observable<BasicResponse<GoogleInfoBean>>

    @POST("api/user.security/verify")
    fun verifyGoogleCode(@Body map: HashMap<String, Any>): Observable<BasicResponse<Boolean>>

    @POST("api/passport/reset")
    fun resetLoginPassword(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/user.security/change")
    fun changePassword(@Body map: HashMap<String, Any>): Observable<BasicResponse<UserBean>>

    @GET("api/user.assets/list")
    fun assetsList(): Observable<BasicResponse<ArrayList<AssetsBean>>>

    @GET("api/home/pos")
    fun homeData(): Observable<BasicResponse<HomeDataBean>>

    @POST("api/pledge/cancel")
    fun cancelPledge(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/pledge/submit")
    fun pledge(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @GET("api/pledge/data")
    fun pledgeData(): Observable<BasicResponse<PledgeDataBean>>

    @POST("api/pledge/fetch")
    fun freezeList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<FreezeBean>>>

    @POST("api/pledge/unlock")
    fun unlock(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>


    @POST("api/user.rank/fetch")
    fun poolNodeRank(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<PoolNodeRankBean>>>


    @POST("api/user.assets/transfer")
    fun transfer(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/user.assets/tips")
    fun transferInfo(@Body map: HashMap<String, Any>): Observable<BasicResponse<TransferFeeBean>>

    @POST("api/user.assets/logs")
    fun transferList(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<TransferBean>>>

    @POST("api/user/referee")
    fun inviteDetail(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<InviteBean>>>

    @POST("api/observer/create")
    fun createObserver(@Body map: HashMap<String, Any>): Observable<BasicResponse<ObserverBean>>

    @POST("api/observer/fetch")
    fun getObservers(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<ObserverBean>>>

    @POST("api/observer/destroy")
    fun destroyObserver(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/observer/save")
    fun updateObserver(@Body map: HashMap<String, Any>): Observable<BasicResponse<Any>>

    @POST("api/observer/subscribe")
    fun subscribeObserver(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<ObserverPermissionRecordBean>>>

    @POST("api/user.income/fetch")
    fun fetchIncome(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<ObserverPermissionRecordBean>>>


    @POST("api/observer/data")
    fun observerData(@Body map: HashMap<String, Any>): Observable<BasicResponse<ObserverPermissionRecordBean>>


    @GET("api/chain/overview")
    fun chainOverview(): Observable<BasicResponse<ChainDataBean>>

    @POST("api/chain/balance")
    fun chainNode(@Body map: HashMap<String, Any>): Observable<BasicResponse<ChainNodeBean>>

    @POST("api/chain/block")
    fun chainBlock(@Body map: HashMap<String, Any>): Observable<BasicResponse<ArrayList<ChainBlockBean>>>

    @GET("api/calculator/meta")
    fun calculator(): Observable<BasicResponse<CalculatorBean>>

    @GET("api/user/profile")
    fun userProfile(): Observable<BasicResponse<UserBean>>

    @GET("api/user.income/overview")
    fun incomeOverview(): Observable<BasicResponse<IncomeBean>>

    @GET("api/app/protocol")
    fun appProtocol(): Observable<BasicResponse<QuestionBean>>

    @GET("api/app/meta")
    fun appMeta(): Observable<BasicResponse<MetaBean>>
}