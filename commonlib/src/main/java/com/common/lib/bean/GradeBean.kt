package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class GradeBean : Serializable {

    var id: String? = null
    var name: String? = null
    var name_en: String? = null

    fun getNameStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(name_en)) {
            return name_en
        }
        return name
    }
}