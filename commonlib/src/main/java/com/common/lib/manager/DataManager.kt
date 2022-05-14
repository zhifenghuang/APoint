package com.common.lib.manager

import android.text.TextUtils
import com.common.lib.bean.*
import com.common.lib.utils.PrefUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DataManager private constructor() {


    private val mGson = Gson()

    private var mMyInfo: UserBean? = null

    companion object {
        const val TAG = "DataManager"

        @Volatile
        private var instance: DataManager? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: DataManager()
                        .also { instance = it }
            }
    }

    fun getGson(): Gson {
        return mGson
    }


    fun getToken(): String? {
        return PrefUtil.getString(
            ConfigurationManager.getInstance().getContext(),
            "token", ""
        )
    }

    fun saveToken(token: String?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "token", token ?: ""
        )
    }

    fun saveUrls(vararg urls: String) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "main_server_url", urls[0]
        )
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "splash_url", urls[1]
        )
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "splash_url_en", urls[1]
        )
    }

    fun getMainServerUrl(): String {
        return "http://app.1314pool.com/"
//        val url = PrefUtil.getString(
//            ConfigurationManager.getInstance().getContext(),
//            "main_server_url", ""
//        )
//        return if (TextUtils.isEmpty(url)) {
//            "http://39.108.8.95/api/"
//        } else {
//            url
//        }
    }

    fun getSplashUrl(): String {
        return PrefUtil.getString(
            ConfigurationManager.getInstance().getContext(),
            if (getLanguage() == 0) {
                "splash_url_en"
            } else {
                "splash_url"
            }, ""
        )
    }

    fun saveMyInfo(myInfo: UserBean?) {
        if (myInfo == null) {
            mMyInfo = null
            PrefUtil.putString(ConfigurationManager.getInstance().getContext(), "user", "")
            return
        }
        mMyInfo = myInfo
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "user",
            mGson.toJson(myInfo)
        )
    }

    fun getMyInfo(): UserBean? {
        if (mMyInfo == null) {
            val str =
                PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "user", "")
            if (TextUtils.isEmpty(str)) {
                return null
            }
            mMyInfo = mGson.fromJson(str, UserBean::class.java)
        }
        return mMyInfo
    }

    fun savePoster(poster: PosterBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "poster",
            if (poster == null) {
                ""
            } else {
                mGson.toJson(poster)
            }
        )
    }

    fun getPoster(): PosterBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "poster", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, PosterBean::class.java)
    }

    fun saveChainData(data: ChainDataBean) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "chain_data",
            mGson.toJson(data)
        )
    }

    fun getChainData(): ChainDataBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "chain_data", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, ChainDataBean::class.java)
    }

    fun saveChainNode(address: String, data: ChainNodeBean) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            address,
            mGson.toJson(data)
        )
    }

    fun getChainNode(address: String): ChainNodeBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), address, "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, ChainNodeBean::class.java)
    }

    fun saveBlockList(list: ArrayList<ChainBlockBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "chain_block_list", if (list == null) {
                ""
            } else {
                mGson.toJson(list)
            }
        )
    }

    fun getBlockList(): ArrayList<ChainBlockBean> {
        val str =
            PrefUtil.getString(
                ConfigurationManager.getInstance().getContext(),
                "chain_block_list",
                ""
            )
        return if (TextUtils.isEmpty(str)) {
            ArrayList<ChainBlockBean>()
        } else {
            mGson.fromJson<ArrayList<ChainBlockBean>>(
                str,
                object : TypeToken<ArrayList<ChainBlockBean>>() {}.type
            )
        }
    }

    fun saveLanguage(language: Int) {
        PrefUtil.putInt(
            ConfigurationManager.getInstance().getContext(),
            "language",
            language
        )
    }

    fun getLanguage(): Int {
        return PrefUtil.getInt(
            ConfigurationManager.getInstance().getContext(),
            "language",
            1
        )
    }

    fun saveHomePosData(homeDataBean: HomeDataBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "home_pos_data",
            if (homeDataBean == null) {
                ""
            } else {
                mGson.toJson(homeDataBean)
            }
        )
    }

    fun getHomePosData(): HomeDataBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "home_pos_data", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, HomeDataBean::class.java)
    }

    fun saveAboutUs(bean: QuestionBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "about_us",
            if (bean == null) {
                ""
            } else {
                mGson.toJson(bean)
            }
        )
    }

    fun getAboutUs(): QuestionBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "about_us", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, QuestionBean::class.java)
    }

    fun saveProtocol(bean: QuestionBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "protocol",
            if (bean == null) {
                ""
            } else {
                mGson.toJson(bean)
            }
        )
    }

    fun getProtocol(): QuestionBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "protocol", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, QuestionBean::class.java)
    }

    fun saveAppMeta(bean: MetaBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "app_meta",
            if (bean == null) {
                ""
            } else {
                mGson.toJson(bean)
            }
        )
    }

    fun getAppMeta(): MetaBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "app_meta", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, MetaBean::class.java)
    }

    fun saveFAQs(list: ArrayList<QuestionBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "faqs", if (list == null) {
                ""
            } else {
                mGson.toJson(list)
            }
        )
    }

    fun getFAQs(): ArrayList<QuestionBean> {
        val str =
            PrefUtil.getString(
                ConfigurationManager.getInstance().getContext(),
                "faqs",
                ""
            )
        return if (TextUtils.isEmpty(str)) {
            ArrayList<QuestionBean>()
        } else {
            mGson.fromJson<ArrayList<QuestionBean>>(
                str,
                object : TypeToken<ArrayList<QuestionBean>>() {}.type
            )
        }
    }


    fun saveUtgPrice(price: String) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "utg_price",
            price
        )
    }

    fun getUtgPrice(): String {
        return PrefUtil.getString(
            ConfigurationManager.getInstance().getContext(),
            "utg_price",
            "56"
        )
    }

    fun saveAddress(address: String) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "address",
            address
        )
    }

    fun getAddress(): String {
        return PrefUtil.getString(
            ConfigurationManager.getInstance().getContext(),
            "address",
            ""
        )
    }

    fun saveLoginUsers(map: HashMap<String, UserBean>) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "login_users", mGson.toJson(map)
        )
    }

    fun saveAssets(assets: ArrayList<AssetsBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "my_assets", if (assets == null) {
                ""
            } else {
                mGson.toJson(assets)
            }
        )
    }

    fun getAssets(): ArrayList<AssetsBean> {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "my_assets", "")
        return if (TextUtils.isEmpty(str)) {
            ArrayList<AssetsBean>()
        } else {
            mGson.fromJson<ArrayList<AssetsBean>>(
                str,
                object : TypeToken<ArrayList<AssetsBean>>() {}.type
            )
        }
    }

    fun saveBanners(banners: ArrayList<BannerBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "banners", if (banners == null) {
                ""
            } else {
                mGson.toJson(banners)
            }
        )
    }

    fun getBanners(): ArrayList<BannerBean> {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "banners", "")
        return if (TextUtils.isEmpty(str)) {
            ArrayList<BannerBean>()
        } else {
            mGson.fromJson<ArrayList<BannerBean>>(
                str,
                object : TypeToken<ArrayList<BannerBean>>() {}.type
            )
        }
    }

    fun saveNoticeList(banners: ArrayList<NoticeBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "notice_list", if (banners == null) {
                ""
            } else {
                mGson.toJson(banners)
            }
        )
    }

    fun getNoticeList(): ArrayList<NoticeBean> {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "notice_list", "")
        return if (TextUtils.isEmpty(str)) {
            ArrayList<NoticeBean>()
        } else {
            mGson.fromJson<ArrayList<NoticeBean>>(
                str,
                object : TypeToken<ArrayList<NoticeBean>>() {}.type
            )
        }
    }

    fun saveIncome(incomeBean: IncomeBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "income_bean", if (incomeBean == null) {
                ""
            } else {
                mGson.toJson(incomeBean)
            }
        )
    }

    fun getIncome(): IncomeBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "income_bean", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, IncomeBean::class.java)
    }


    fun getLoginUsers(): HashMap<String, UserBean> {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "login_users", "")
        val map = if (TextUtils.isEmpty(str)) {
            HashMap<String, UserBean>()
        } else {
            mGson.fromJson<HashMap<Long, UserBean>>(
                str,
                object : TypeToken<HashMap<String, UserBean>>() {}.type
            )
        } as HashMap<String, UserBean>
        val bean = getMyInfo()
        if (bean != null) {
            map[bean.userId!!] = bean
        }
        if (TextUtils.isEmpty(str)) {
            saveLoginUsers(map)
        }
        return map
    }


    fun logout() {
        saveToken("")
        saveAddress("")
        saveMyInfo(null)
        saveAssets(null)
        savePoster(null)
        saveIncome(null)
        saveAppMeta(null)
        saveHomePosData(null)
    }
}