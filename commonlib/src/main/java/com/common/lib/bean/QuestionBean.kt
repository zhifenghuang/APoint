package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class QuestionBean : Serializable {


    var id: Int = 0


    var content: String? = null

    var content_en: String? = null

    var title: String? = null

    var title_en: String? = null

    var subTitle: String? = null

    var subTitle_en: String? = null

    var isShowContent = false

    var topic: TopicBean? = null

    fun getTitleStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(title_en)) {
            return title_en
        }
        return title
    }

    fun getSubTitleStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(subTitle_en)) {
            return subTitle_en
        }
        return subTitle
    }

    fun getContentStr(): String? {
        if (DataManager.getInstance().getLanguage() == 0 && !TextUtils.isEmpty(content_en)) {
            return content_en
        }
        return content
    }

}