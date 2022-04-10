package com.common.lib.bean
import java.io.Serializable

class FreezeBean : Serializable {
    var id: Int = 0

    var userId: String? = null

    var orderNo: String? = null

    var amount: String? = null

    var fee: String? = null

    var address: String? = null

    var assetsId: String? = null

    var symbol: String? = null

    var type: String? = null

    var unfreezeTime: String? = null

    var createTime: String? = null
}