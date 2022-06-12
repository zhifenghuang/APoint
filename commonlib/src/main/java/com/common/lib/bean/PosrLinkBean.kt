package com.common.lib.bean

import java.io.Serializable
import java.math.BigDecimal

class PosrLinkBean : Serializable {

    var link: String? = null
    var userId: String? = null
    var type: String? = null
    var POSR_RevenuesAddress: String? = null
    var createTime: String? = null
    var expireTime: String? = null
}