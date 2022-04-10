package com.common.lib.bean

import java.io.Serializable

class ObserverBean : Serializable {
    var id: String? = null

    var observerId: String? = null

    var name: String? = null

    var userId: String? = null

    var remark: String? = null

    var type: String? = null

    var account: String? = null

    var symbol: String? = null

    var link: String? = null

    var createTime: String? = null

    var permission: PermissionBean? = null
}