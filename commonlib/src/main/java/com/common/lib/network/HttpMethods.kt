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
                    "Accept-Language",
                    if (DataManager.getInstance().getLanguage() == 0) {
                        "en-us"
                    } else {
                        "zh-cn"
                    }
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

    fun register(
        loginAccount: String, loginPassword: String, refereeId: String, verifyCode: String,
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val map = HashMap<String, Any>()
        map["loginAccount"] = loginAccount
        map["loginPassword"] = loginPassword
        map["refereeId"] = refereeId
        map["verifyCode"] = verifyCode
        val observable = api.register(map)
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
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["loginAccount"] = loginAccount
        map["loginPassword"] = loginPassword
        map["reLoginPassword"] = reLoginPassword
        map["verifyCode"] = verifyCode
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

    fun bannerList(
        type: String,
        observer: HttpObserver<BasicResponse<ArrayList<BannerBean>>, ArrayList<BannerBean>>
    ) {
        val map = HashMap<String, Any>()
        map["type"] = type
        val observable = api.bannerList(map)
        toSubscribe(observable, observer)
    }

    fun poster(
        observer: HttpObserver<BasicResponse<PosterBean>, PosterBean>
    ) {
        val observable = api.poster()
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


    fun inviteDetail(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<InviteBean>>, ArrayList<InviteBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        val observable = api.inviteDetail(map)
        toSubscribe(observable, observer)
    }

    fun incomeRecord(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<IncomeBean>>, ArrayList<IncomeBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        val observable = api.incomeRecord(map)
        toSubscribe(observable, observer)
    }

    fun incomeOverview(
        observer: HttpObserver<BasicResponse<InviteBean>, InviteBean>
    ) {
        val observable = api.incomeOverview()
        toSubscribe(observable, observer)
    }


    fun userProfile(
        observer: HttpObserver<BasicResponse<UserBean>, UserBean>
    ) {
        val observable = api.userProfile()
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

    fun goodsList(
        pageIndex: Int,
        goodsType: Int,
        pageSize: Int,
        observer: HttpObserver<BasicResponse<ArrayList<GoodsBean>>, ArrayList<GoodsBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["goodsType"] = goodsType
        map["pageSize"] = pageSize
        val observable = api.goodsList(map)
        toSubscribe(observable, observer)
    }

    fun searchGoods(
        keywords: String,
        observer: HttpObserver<BasicResponse<ArrayList<GoodsBean>>, ArrayList<GoodsBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = 1
        map["keywords"] = keywords
        map["pageSize"] = Int.MAX_VALUE
        val observable = api.goodsList(map)
        toSubscribe(observable, observer)
    }

    fun goodsDetail(
        id: Int,
        observer: HttpObserver<BasicResponse<GoodsBean>, GoodsBean>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        val observable = api.goodsDetail(map)
        toSubscribe(observable, observer)
    }

    fun submitOrder(
        addressId: Int,
        orderType: Int,
        payType: String,
        payPassword: String,
        goods: List<HashMap<String, Any>>,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["addressId"] = addressId
        map["orderType"] = orderType
        map["payType"] = payType
        map["goods"] = goods
        map["payPassword"] = payPassword
        val observable = api.submitOrder(map)
        toSubscribe(observable, observer)
    }

    fun orderList(
        pageIndex: Int,
        deliveryStatus: Int,
        observer: HttpObserver<BasicResponse<ArrayList<OrderBean>>, ArrayList<OrderBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 20
        if (deliveryStatus > 0) {
            map["deliveryStatus"] = deliveryStatus
        }
        val observable = api.orderList(map)
        toSubscribe(observable, observer)
    }

    fun orderComplete(
        id: Int,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        val observable = api.orderComplete(map)
        toSubscribe(observable, observer)
    }

    fun orderLogistics(
        id: Int,
        expressCode: String,
        expressNo: String,
        observer: HttpObserver<BasicResponse<OrderBean>, OrderBean>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        map["expressCode"] = expressCode
        map["expressNo"] = expressNo
        val observable = api.orderLogistics(map)
        toSubscribe(observable, observer)
    }

    fun addressList(
        pageIndex: Int,
        observer: HttpObserver<BasicResponse<ArrayList<ReceiveAddressBean>>, ArrayList<ReceiveAddressBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = pageIndex
        map["pageSize"] = 100
        val observable = api.addressList(map)
        toSubscribe(observable, observer)
    }


    fun defaultAddress(
        observer: HttpObserver<BasicResponse<ArrayList<ReceiveAddressBean>>, ArrayList<ReceiveAddressBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pageIndex"] = 1
        map["pageSize"] = 1
        val observable = api.addressList(map)
        toSubscribe(observable, observer)
    }


    fun districtList(
        pCode: String,
        observer: HttpObserver<BasicResponse<ArrayList<DistrictBean>>, ArrayList<DistrictBean>>
    ) {
        val map = HashMap<String, Any>()
        map["pCode"] = pCode
        val observable = api.districtList(map)
        toSubscribe(observable, observer)
    }

    fun addAddress(
        name: String,
        mobile: String,
        provinceCode: String,
        provinceName: String,
        cityCode: String,
        cityName: String,
        districtCode: String,
        districtName: String,
        detail: String,
        default: Int,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["name"] = name
        map["mobile"] = mobile
        map["provinceCode"] = provinceCode
        map["provinceName"] = provinceName
        map["cityCode"] = cityCode
        map["cityName"] = cityName
        map["districtCode"] = districtCode
        map["districtName"] = districtName
        map["detail"] = detail
        map["default"] = default
        val observable = api.addAddress(map)
        toSubscribe(observable, observer)
    }

    fun saveAddress(
        id: Int,
        name: String,
        mobile: String,
        provinceCode: String,
        provinceName: String,
        cityCode: String,
        cityName: String,
        districtCode: String,
        districtName: String,
        detail: String,
        default: Int,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        map["name"] = name
        map["mobile"] = mobile
        map["provinceCode"] = provinceCode
        map["provinceName"] = provinceName
        map["cityCode"] = cityCode
        map["cityName"] = cityName
        map["districtCode"] = districtCode
        map["districtName"] = districtName
        map["detail"] = detail
        map["default"] = default
        val observable = api.saveAddress(map)
        toSubscribe(observable, observer)
    }

    fun resetDefaultAddress(
        id: Int,
        default: Int,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        map["default"] = default
        val observable = api.saveAddress(map)
        toSubscribe(observable, observer)
    }

    fun deleteAddress(
        id: Int,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["id"] = id
        val observable = api.deleteAddress(map)
        toSubscribe(observable, observer)
    }


    fun ticker(
        observer: HttpObserver<BasicResponse<ArrayList<QuotationsBean>>, ArrayList<QuotationsBean>>
    ) {
        val observable = api.ticker()
        toSubscribe(observable, observer)
    }

    fun exchange(
        payPassword: String,
        fromSymbol: String,
        toSymbol: String,
        amount: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["payPassword"] = payPassword
        map["fromSymbol"] = fromSymbol
        map["amount"] = amount
        map["toSymbol"] = toSymbol
        val observable = api.exchange(map)
        toSubscribe(observable, observer)
    }

    fun assetsExchangeInfo(
        fromSymbol: String,
        toSymbol: String,
        observer: HttpObserver<BasicResponse<ExchangeTipsBean>, ExchangeTipsBean>
    ) {
        val map = HashMap<String, Any>()
        map["fromSymbol"] = fromSymbol
        map["toSymbol"] = toSymbol
        val observable = api.assetsExchangeInfo(map)
        toSubscribe(observable, observer)
    }

    fun agentGoods(
        observer: HttpObserver<BasicResponse<ArrayList<AgentBean>>, ArrayList<AgentBean>>
    ) {
        val observable = api.agentGoods()
        toSubscribe(observable, observer)
    }

    fun agentSubmit(
        id: String,
        code: String,
        payPassword: String,
        districtText: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val map = HashMap<String, Any>()
        map["payPassword"] = payPassword
        map["districtText"] = districtText
        map["id"] = id
        map["code"] = code
        val observable = api.agentSubmit(map)
        toSubscribe(observable, observer)
    }

    fun checkInOverview(
        observer: HttpObserver<BasicResponse<CheckInBean>, CheckInBean>
    ) {
        val observable = api.checkInOverview()
        toSubscribe(observable, observer)
    }

    fun checkInSubmit(
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.checkInSubmit()
        toSubscribe(observable, observer)
    }

    fun contactUs(
        observer: HttpObserver<BasicResponse<NoticeBean>, NoticeBean>
    ) {
        val observable = api.contactUs()
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