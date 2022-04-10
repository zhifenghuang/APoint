package com.common.lib.bean

import java.io.Serializable

class OrderBean : Serializable {
    var id: String? = null
    var orderNo: String? = null
    var userId: String? = null
    var address: String? = null
    var assetsId: String? = null
    var symbol: String? = null
    var amount: String? = null
    var unit: String? = null
    var totalAmount: String? = null
    var type: String? = null
    var cancelTime: String? = null
    var payTime: String? = null
    var orderState: Int = 0
    var buyerId: String? = null
    var fee: String? = null
    var payAmount: String? = null
    var paySymbol: String? = null
    var obtainAmount: String? = null
    var createTime: String? = null
}