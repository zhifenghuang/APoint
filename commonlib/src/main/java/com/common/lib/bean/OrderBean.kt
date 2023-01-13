package com.common.lib.bean

import java.io.Serializable

class OrderBean : Serializable {
    var id: Int = 0
    var orderNo: String? = null
    var orderType: String? = null
    var userId: String? = null
    var orderStatus: String? = null
    var payType: String? = null
    var payStatus: String? = null
    var deliveryStatus: Int = 0
    var payTime: String? = null

    var payAmount: String? = null
    var goodsAmount: String? = null
    var discountAmount: String? = null
    var deliveryAmount: String? = null
    var commissionStatus: String? = null
    var pv: String? = null
    var createTime: String? = null
    var goods: List<HashMap<String, Any>>? = null

    var address: ReceiveAddressBean? = null

    var expressCode: String? = null
    var expressNo: String? = null
    var expressName: String? = null

    var expressCode_1: String? = null
    var expressNo_1: String? = null
    var expressName_1: String? = null

    var expressCode_2: String? = null
    var expressNo_2: String? = null
    var expressName_2: String? = null

    var expressCode_3: String? = null
    var expressNo_3: String? = null
    var expressName_3: String? = null

    var expressTime: String? = null
    var logistics: List<OrderLogisticsBean>? = null
}