package com.common.lib.network

import android.text.TextUtils
import com.common.lib.bean.*
import com.common.lib.manager.DataManager
import com.common.lib.utils.LogUtil
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.jvm.Throws


class HttpMethods private constructor() {

    private val TAG: String = "HttpMethods"

    private val api: Api

    private var mRetrofit: Retrofit? = null
    private val mBuilder: OkHttpClient.Builder

    companion object {
        const val TAG = "HttpMethods"
        const val CONNECT_TIMEOUT: Long = 90
        const val WRITE_TIMEOUT: Long = 90
        const val READ_TIMEOUT: Long = 90

        @Volatile
        private var instance: HttpMethods? = null

        fun getInstance() =
            instance
                ?: synchronized(this) {
                    instance
                        ?: HttpMethods()
                            .also { instance = it }
                }
    }

    init {
        mBuilder = OkHttpClient.Builder()
        val loggingInterceptor =
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtil.LogE(message)
                }
            })
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        val interceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder = chain.request()
                    .newBuilder()
                val token = DataManager.getInstance().getToken()
                if (!TextUtils.isEmpty(token)) {
                    builder.addHeader("Authorization", token!!)
                }
                builder.addHeader(
                    "Accept-Language", "zh-cn"
//                    if (DataManager.getInstance().getLanguage() == 0) {
//                        "en-us"
//                    } else {
//                        "zh-cn"
//                    }
                )
                builder.addHeader("Platform", "APP_ANDROID")
                return chain.proceed(builder.build())
            }
        }
        mBuilder
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
        resetRetrofit()
        api = mRetrofit!!.create(Api::class.java)
    }

    fun resetRetrofit() {
        mRetrofit = Retrofit.Builder()
            .client(mBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(DataManager.getInstance().getMainServerUrl())//
            .build()
    }

    fun checkVersion(
        versionCode: Int, versionName: String,
        observer: HttpObserver<BasicResponse<VersionBean>, VersionBean>
    ) {
        val map = HashMap<String, Any>()
        map["versionCode"] = versionCode
        map["versionName"] = versionName
        val observable = api.checkVersion(map)
        toSubscribe(observable, observer)
    }

    fun getCaptcha(
        observer: HttpObserver<BasicResponse<PicCodeBean>, PicCodeBean>
    ) {
        val observable = api.getCaptcha()
        toSubscribe(observable, observer)
    }

    fun register1(
        loginAccount: String, refereeId: String, code: String, key: String,
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val map = HashMap<String, Any>()
        map["loginAccount"] = loginAccount
        map["refereeId"] = refereeId
        map["code"] = code
        map["key"] = key
        val observable = api.register1(map)
        toSubscribe(observable, observer)
    }

    fun sendEmail(
        loginAccount: String,
        scene: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["loginAccount"] = loginAccount
        map["scene"] = scene
        val observable = api.sendEmail(map)
        toSubscribe(observable, observer)
    }

    fun register2(
        hash: String,
        loginPassword: String,
        reLoginPassword: String,
        verifyCode: String,
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val map = HashMap<String, Any>()
        map["hash"] = hash
        map["loginPassword"] = loginPassword
        map["reLoginPassword"] = reLoginPassword
        map["verifyCode"] = verifyCode
        val observable = api.register2(map)
        toSubscribe(observable, observer)
    }

    fun login(
        loginAccount: String, loginPassword: String, code: String, key: String,
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val map = HashMap<String, Any>()
        map["loginAccount"] = loginAccount
        map["loginPassword"] = loginPassword
        map["code"] = code
        map["key"] = key
        val observable = api.login(map)
        toSubscribe(observable, observer)
    }

    fun restLoginPsw(
        loginAccount: String,
        loginPassword: String,
        reLoginPassword: String,
        verifyCode: String,
        key: String,
        code: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["loginAccount"] = loginAccount
        map["loginPassword"] = loginPassword
        map["reLoginPassword"] = reLoginPassword
        map["verifyCode"] = verifyCode
        map["code"] = code
        map["key"] = key
        val observable = api.resetLoginPassword(map)
        toSubscribe(observable, observer)
    }

    fun changePsw(
        verifyCode: String,
        password: String,
        rePassword: String,
        scene: String,
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val map = HashMap<String, Any>()
        map["verifyCode"] = verifyCode
        map["password"] = password
        map["rePassword"] = rePassword
        map["scene"] = scene
        val observable = api.changePassword(map)
        toSubscribe(observable, observer)
    }

    fun noticeList(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<NoticeBean>>, ArrayList<NoticeBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        val observable = api.noticeList(map)
        toSubscribe(observable, observer)
    }

    fun readNotice(
        id: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        val observable = api.readNotice(map)
        toSubscribe(observable, observer)
    }

    fun bannerList(
        type: String,
        observer: HttpObserver<BasicResponse<ArrayList<BannerBean>>, ArrayList<BannerBean>>
    ) {
        val map = HashMap<String, Any>()
        map["type"] = type
        val observable = api.bannerList(map)
        toSubscribe(observable, observer)
    }

    fun getGoogleCode(
        observer: HttpObserver<BasicResponse<GoogleInfoBean>, GoogleInfoBean>
    ) {
        val observable = api.getGoogleCode()
        toSubscribe(observable, observer)
    }

    fun poster(
        observer: HttpObserver<BasicResponse<PosterBean>, PosterBean>
    ) {
        val observable = api.poster()
        toSubscribe(observable, observer)
    }

    fun serviceHelp(
        observer: HttpObserver<BasicResponse<QuestionBean>, QuestionBean>
    ) {
        val observable = api.serviceHelp()
        toSubscribe(observable, observer)
    }

    fun aboutUs(
        observer: HttpObserver<BasicResponse<QuestionBean>, QuestionBean>
    ) {
        val observable = api.aboutUs()
        toSubscribe(observable, observer)
    }

    fun faq(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<QuestionBean>>, ArrayList<QuestionBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 50
        val observable = api.faq(map)
        toSubscribe(observable, observer)
    }

    fun verifyGoogleCode(
        code: String,
        observer: HttpObserver<BasicResponse<Boolean>, Boolean>
    ) {
        val map = HashMap<String, Any>()
        map["code"] = code
        val observable = api.verifyGoogleCode(map)
        toSubscribe(observable, observer)
    }

    fun assetsList(
        observer: HttpObserver<BasicResponse<ArrayList<AssetsBean>>, ArrayList<AssetsBean>>
    ) {
        val observable = api.assetsList();
        toSubscribe(observable, observer)
    }

    fun homeData(
        observer: HttpObserver<BasicResponse<ArrayList<HomeDataBean>>, ArrayList<HomeDataBean>>
    ) {
        val observable = api.homeData();
        toSubscribe(observable, observer)
    }

    fun pledge(
        amount: String,
        payPassword: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["payPassword"] = payPassword
        map["amount"] = amount
        val observable = api.pledge(map)
        toSubscribe(observable, observer)
    }

    fun posrPledge(
        amount: String,
        payPassword: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["payPassword"] = payPassword
        map["amount"] = amount
        val observable = api.posrPledge(map)
        toSubscribe(observable, observer)
    }

    fun posrAddStorage(
        address: String,
        type: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["address"] = address
        map["type"] = type
        val observable = api.posrAddStorage(map)
        toSubscribe(observable, observer)
    }

    fun posrCancelStorage(
        id: String,
        address: String,
        payPassword: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["address"] = address
        map["id"] = id
        map["payPassword"] = payPassword
        val observable = api.posrCancelStorage(map)
        toSubscribe(observable, observer)
    }

    fun storageList(
        pageIndex: Int,
        type: String,  //类型 HOSTING托管 OWNER自管
        observer: HttpObserver<BasicResponse<ArrayList<StorageBean>>, ArrayList<StorageBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["type"] = type
        map["pageSize"] = 50
        val observable = api.storageList(map)
        toSubscribe(observable, observer)
    }

    fun getPosrLink(
        type: String,  //类型 HOSTING托管 OWNER自管
        observer: HttpObserver<BasicResponse<PosrLinkBean>, PosrLinkBean>
    ) {
        val map = HashMap<String, Any>()
        map["type"] = type
        val observable = api.getPosrLink(map)
        toSubscribe(observable, observer)
    }

    fun freezeList(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<FreezeBean>>, ArrayList<FreezeBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        val observable = api.freezeList(map)
        toSubscribe(observable, observer)
    }

    fun unlock(
        id: Int,
        payPassword: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        map["payPassword"] = payPassword
        val observable = api.unlock(map)
        toSubscribe(observable, observer)
    }

    fun poolNodeRank(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<PoolNodeRankBean>>, ArrayList<PoolNodeRankBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        val observable = api.poolNodeRank(map)
        toSubscribe(observable, observer)
    }

    fun pledgeData(
        observer: HttpObserver<BasicResponse<PledgeDataBean>, PledgeDataBean>
    ) {
        val observable = api.pledgeData()
        toSubscribe(observable, observer)
    }

    fun cancelPledge(
        freeze: Int,
        amount: String,
        payPassword: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["freeze"] = freeze
        map["payPassword"] = payPassword
        map["amount"] = amount
        val observable = api.cancelPledge(map)
        toSubscribe(observable, observer)
    }

    fun cancelPosrPledge(
        freeze: Int,
        amount: String,
        payPassword: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["freeze"] = freeze
        map["payPassword"] = payPassword
        map["amount"] = amount
        val observable = api.cancelPosrPledge(map)
        toSubscribe(observable, observer)
    }

    fun transferList(
        symbol: String,
        pageIndex: Int,
        type: ArrayList<Int>?,
        observer: HttpObserver<BasicResponse<ArrayList<TransferBean>>, ArrayList<TransferBean>>
    ) {
        val map = HashMap<String, Any>()
        map["symbol"] = symbol
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        if (type != null && !type.isEmpty())
            map["type"] = type
        val observable = api.transferList(map)
        toSubscribe(observable, observer)
    }

    fun transferInfo(
        symbol: String,
        observer: HttpObserver<BasicResponse<TransferFeeBean>, TransferFeeBean>
    ) {
        val map = HashMap<String, Any>()
        map["symbol"] = symbol
        val observable = api.transferInfo(map)
        toSubscribe(observable, observer)
    }

    fun transfer(
        payPassword: String,
        symbol: String,
        amount: String,
        address: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["payPassword"] = payPassword
        map["symbol"] = symbol
        map["amount"] = amount
        map["address"] = address
        val observable = api.transfer(map)
        toSubscribe(observable, observer)
    }

    fun awardRule(
        observer: HttpObserver<BasicResponse<QuestionBean>, QuestionBean>
    ) {
        val observable = api.awardRule()
        toSubscribe(observable, observer)
    }


    fun inviteDetail(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<InviteBean>>, ArrayList<InviteBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 50
        val observable = api.inviteDetail(map)
        toSubscribe(observable, observer)
    }

    fun createObserver(
        type: String,
        remark: String,
        permission: HashMap<String, Boolean>,
        observer: HttpObserver<BasicResponse<ObserverBean>, ObserverBean>
    ) {
        val map = HashMap<String, Any>()
        map["type"] = type
        map["remark"] = remark
        map["permission"] = permission
        val observable = api.createObserver(map)
        toSubscribe(observable, observer)
    }

    fun destroyObserver(
        id: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        val observable = api.destroyObserver(map)
        toSubscribe(observable, observer)
    }

    fun createCollection(
        type: String,
        remark: String,
        link: String,
        observer: HttpObserver<BasicResponse<ObserverBean>, ObserverBean>
    ) {
        val map = HashMap<String, Any>()
        map["type"] = type
        map["remark"] = remark
        map["link"] = link
        val observable = api.createObserver(map)
        toSubscribe(observable, observer)
    }

    fun getObservers(
        pageIndex: Int,
        type: String,
        observer: HttpObserver<BasicResponse<ArrayList<ObserverBean>>, ArrayList<ObserverBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        map["type"] = type
        val observable = api.getObservers(map)
        toSubscribe(observable, observer)
    }

    fun updateObserver(
        id: String,
        remark: String,
        income: Boolean,
        account: Boolean,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        val itemMap = HashMap<String, Boolean>()
        itemMap["income"] = income
        itemMap["account"] = account
        map["permission"] = itemMap
        map["id"] = id
        map["remark"] = remark
        val observable = api.updateObserver(map)
        toSubscribe(observable, observer)
    }

    fun subscribeObserver(
        pageIndex: Int,
        type: String,
        observerId: String,
        observer: HttpObserver<BasicResponse<ArrayList<ObserverPermissionRecordBean>>, ArrayList<ObserverPermissionRecordBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        map["type"] = type
        map["observerId"] = observerId
        val observable = api.subscribeObserver(map)
        toSubscribe(observable, observer)
    }

    fun observerData(
        type: String,
        observerId: String,
        observer: HttpObserver<BasicResponse<ObserverPermissionRecordBean>, ObserverPermissionRecordBean>
    ) {
        val map = HashMap<String, Any>()
        map["type"] = type
        map["observerId"] = observerId
        val observable = api.observerData(map)
        toSubscribe(observable, observer)
    }

    fun fetchIncome(
        pageIndex: Int,
        subType: ArrayList<Int>,
        observer: HttpObserver<BasicResponse<ArrayList<ObserverPermissionRecordBean>>, ArrayList<ObserverPermissionRecordBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        if (subType.size > 0) {
            map["subType"] = subType
        }
        val observable = api.fetchIncome(map)
        toSubscribe(observable, observer)
    }

    fun chainOverview(
        observer: HttpObserver<BasicResponse<ChainDataBean>, ChainDataBean>
    ) {
        val observable = api.chainOverview()
        toSubscribe(observable, observer)
    }

    fun chainNode(
        address: String,
        observer: HttpObserver<BasicResponse<ChainNodeBean>, ChainNodeBean>
    ) {
        val map = HashMap<String, Any>()
        map["address"] = address
        val observable = api.chainNode(map)
        toSubscribe(observable, observer)
    }

    fun chainBlock(
        observer: HttpObserver<BasicResponse<ArrayList<ChainBlockBean>>, ArrayList<ChainBlockBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = 1
        map["pageSize"] = 10
        val observable = api.chainBlock(map)
        toSubscribe(observable, observer)
    }

    fun calculator(
        observer: HttpObserver<BasicResponse<CalculatorBean>, CalculatorBean>
    ) {
        val observable = api.calculator()
        toSubscribe(observable, observer)
    }

    fun userProfile(
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val observable = api.userProfile()
        toSubscribe(observable, observer)
    }

    fun incomeOverview(
        observer: HttpObserver<BasicResponse<IncomeBean>, IncomeBean>
    ) {
        val observable = api.incomeOverview()
        toSubscribe(observable, observer)
    }

    fun appProtocol(
        observer: HttpObserver<BasicResponse<QuestionBean>, QuestionBean>
    ) {
        val observable = api.appProtocol()
        toSubscribe(observable, observer)
    }

    fun appMeta(
        observer: HttpObserver<BasicResponse<MetaBean>, MetaBean>
    ) {
        val observable = api.appMeta()
        toSubscribe(observable, observer)
    }

    private fun <T : BasicResponse<Data>, Data> toSubscribe(
        observable: Observable<T>,
        observer: HttpObserver<T, Data>
    ) {
        observable.retry(2) { throwable ->
            throwable is SocketTimeoutException ||
                    throwable is ConnectException ||
                    throwable is TimeoutException
        }.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

}