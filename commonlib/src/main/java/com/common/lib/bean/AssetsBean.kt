package com.common.lib.bean

import java.io.Serializable
import java.math.BigDecimal

class AssetsBean : Serializable {

    var id: String? = null
    var userId: String? = null
    var assetsId: Int = 0
    var symbol: String? = null
    var freeze: String? = null
    var lock: String? = null
    var balance: String? = null
    var internal: String? = null
    var address: String? = null
    var createTime: String? = null
    var status: Int = 0
    var used: Int = 0
    var visible: Int = 0

    fun getTotalBalance(): String? {
        if (balance == null || internal == null) {
            return balance;
        }
        return BigDecimal(balance).add(BigDecimal(internal)).toString()
    }

    fun getSymbol2(): String? {
        if (symbol?.uppercase() == "INTEGRAL") {
            return "A Point";
        }
        return symbol
    }
}