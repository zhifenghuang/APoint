package com.common.lib.bean

import android.text.TextUtils
import com.common.lib.manager.DataManager
import java.io.Serializable

class BannerBean : Serializable {


    var id: String? = null

    var name: String? = null

    var intro: String? = null

    var intro_en: String? = null

    var file: String? = null

    var file_en: String? = null

    var linkUrl: String? = null

    var linkType: String? = null

    var sequence: String? = null

    var createTime: String? = null

    fun getFileStr():String?{
        if(DataManager.getInstance().getLanguage()==0 && !TextUtils.isEmpty(file_en)){
            return file_en
        }
        return file
    }
}