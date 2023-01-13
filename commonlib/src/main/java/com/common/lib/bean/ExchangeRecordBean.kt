package com.common.lib.bean

import java.io.Serializable

class ExchangeRecordBean : Serializable {
    var id: String? = null
    var userId: String? = null
    var orderNo: String? = null
    var amount: String? = null
    var fee: String? = null
    var pv: String? = null
    var address: String? = null
    var assetsId: String? = null
    var symbol: String? = null
    var pledgeTime: String? = null
    var type: Int = 0
    var unfreezeTime: String? = null
    var status: String? = null
    var logId: String? = null
    var createTime: String? = null
    var remark: String? = null
}