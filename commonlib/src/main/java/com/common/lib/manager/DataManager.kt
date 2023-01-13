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

    fun getMainServerUrl(): String {
        return "https://a.uaauaa.io"//"http://uaa-store.dev.zijian6.cn" //
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


    fun getLanguage(): Int {
        return PrefUtil.getInt(
            ConfigurationManager.getInstance().getContext(),
            "language",
            1
        )
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

    fun saveCheckInBean(bean: CheckInBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "check_in",
            if (bean == null) {
                ""
            } else {
                mGson.toJson(bean)
            }
        )
    }

    fun getCheckInBean(): CheckInBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "check_in", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, CheckInBean::class.java)
    }

    fun saveContractUsBean(bean: NoticeBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "contract_us",
            if (bean == null) {
                ""
            } else {
                mGson.toJson(bean)
            }
        )
    }

    fun getContractUsBean(): NoticeBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "contract_us", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, NoticeBean::class.java)
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

    fun saveOrderList(status: Int, list: ArrayList<OrderBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "order_list_$status", if (list == null) {
                ""
            } else {
                mGson.toJson(list)
            }
        )
    }

    fun getOrderList(status: Int): ArrayList<OrderBean> {
        val str =
            PrefUtil.getString(
                ConfigurationManager.getInstance().getContext(),
                "order_list_$status",
                ""
            )
        return if (TextUtils.isEmpty(str)) {
            ArrayList<OrderBean>()
        } else {
            mGson.fromJson<ArrayList<OrderBean>>(
                str,
                object : TypeToken<ArrayList<OrderBean>>() {}.type
            )
        }
    }

    fun saveGoodsList(banners: ArrayList<GoodsBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "goods_list", if (banners == null) {
                ""
            } else {
                mGson.toJson(banners)
            }
        )
    }

    fun savePackageGoodsList(banners: ArrayList<GoodsBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "package_goods_list", if (banners == null) {
                ""
            } else {
                mGson.toJson(banners)
            }
        )
    }

    fun saveSwapGoodsList(banners: ArrayList<GoodsBean>?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "swap_goods_list", if (banners == null) {
                ""
            } else {
                mGson.toJson(banners)
            }
        )
    }

    fun getSwapGoodsList(): ArrayList<GoodsBean> {
        val str =
            PrefUtil.getString(
                ConfigurationManager.getInstance().getContext(),
                "swap_goods_list",
                ""
            )
        return if (TextUtils.isEmpty(str)) {
            ArrayList<GoodsBean>()
        } else {
            mGson.fromJson<ArrayList<GoodsBean>>(
                str,
                object : TypeToken<ArrayList<GoodsBean>>() {}.type
            )
        }
    }

    fun getPackageGoodsList(): ArrayList<GoodsBean> {
        val str =
            PrefUtil.getString(
                ConfigurationManager.getInstance().getContext(),
                "package_goods_list",
                ""
            )
        return if (TextUtils.isEmpty(str)) {
            ArrayList<GoodsBean>()
        } else {
            mGson.fromJson<ArrayList<GoodsBean>>(
                str,
                object : TypeToken<ArrayList<GoodsBean>>() {}.type
            )
        }
    }

    fun getGoodsList(): ArrayList<GoodsBean> {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "goods_list", "")
        return if (TextUtils.isEmpty(str)) {
            ArrayList<GoodsBean>()
        } else {
            mGson.fromJson<ArrayList<GoodsBean>>(
                str,
                object : TypeToken<ArrayList<GoodsBean>>() {}.type
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
        saveMyInfo(null)
        saveAssets(null)
        savePoster(null)
        saveCheckInBean(null)
    }
}