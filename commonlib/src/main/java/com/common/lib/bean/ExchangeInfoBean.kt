package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class ExchangeInfoBean : Serializable {
    var pv: String? = null
    var totalPv: String? = null
    var posrStorageAmount: String? = null
    var exchangeStorageAmount: String? = null
    var releasedAmount: String? = null
}