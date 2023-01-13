package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class ExchangeTipsBean : Serializable {
    var tips: String? = null
    var tips_en: String? = null
    var rate: String? = null

    fun getTipsStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(tips_en)) {
            return tips_en
        }
        return tips
    }
}