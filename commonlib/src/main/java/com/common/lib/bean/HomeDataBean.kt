package com.common.lib.bean

import java.io.Serializable

class HomeDataBean : Serializable {
    var utgBalance: String? = null //余额
    var ultronNode: String? = null//总质押
    var pledgeAmount: String? = null //质押数量
    var myTotalProfit: String? = null //我的总收益
    var totalProfit: String? = null //权益池总收益
    var yesterdayProfit: String? = null //权益池预估收益
    var myYesterdayProfit: String? = null //我的预估收益
}