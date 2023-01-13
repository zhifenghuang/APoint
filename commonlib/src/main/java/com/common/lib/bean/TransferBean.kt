package com.common.lib.bean

import java.io.Serializable

class TransferBean : Serializable {
    var id: String? = null
    var type: Int = 0
    var direction: String? = null
    var fromAddress: String? = null
    var toAddress: String? = null
    var symbol: String? = null
    var fee: String? = null
    var amount: String? = null
    var totalAmount: String? = null
    var freeze: String? = null
    var hash: String? = null
    var remark: String? = null
    var origin: String? = null
    var orderState:Int=-1 //挂单状态  0进行中 1已完成 2已取消
    var status: Int=0  //0审核中 10已放行 20拒绝 30已上链
    var createTime: String? = null
    var updateTime: String? = null

    fun getSymbol2(): String? {
        if (symbol?.uppercase() == "INTEGRAL") {
            return "A Point";
        }
        return symbol
    }
}