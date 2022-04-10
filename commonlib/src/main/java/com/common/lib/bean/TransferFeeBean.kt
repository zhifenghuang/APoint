package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class TransferFeeBean : Serializable {
    var feeRate: String? = null
    var tips: String? = null
    var tips_en: String? = null
    var max: String? = null
    var min: String? = null

    fun getTipsStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(tips_en)) {
            return tips_en
        }
        return tips
    }
}