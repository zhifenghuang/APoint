package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class NoticeBean : Serializable {


    var id: String? = null

    var title: String? = null

    var title_en: String? = null

    var showType: String? = null

    var categoryId: String? = null

    var imageId: String? = null

    var content: String? = null

    var content_en: String? = null

    var sequence: String? = null

    var status: Int = 0

    var virtualViews: String? = null

    var actualViews: String? = null

    var userViews: String? = null

    var createTime: String? = null

    fun getTitleStr():String?{
        if(DataManager.getInstance().getLanguage()==0 && !TextUtils.isEmpty(title_en)){
            return title_en
        }
        return title
    }

    fun getContentStr():String?{
        if(DataManager.getInstance().getLanguage()==0 && !TextUtils.isEmpty(content_en)){
            return content_en
        }
        return content
    }
}