package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class TopicBean : Serializable {


    var id: Int = 0


    var name: String? = null

    var name_en: String? = null

    var questions: ArrayList<QuestionBean>? = null

    var isOpen = true

    var status: Int = 0


    fun getNameStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(name_en)) {
            return name_en
        }
        return name
    }

}