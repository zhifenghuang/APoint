package com.common.lib.bean

import java.io.Serializable

class GoodsBean : Serializable {
    var id: Int = 0
    var goodsName: String? = null
    var goodsNo: String? = null
    var goodsType: Int = 0
    var specType: String? = null
    var coverPic: String? = null
    var marketPrice: String? = null
    var salePrice: String? = null
    var deliveryId: String? = null
    var stockNum: String? = null
    var volume: String? = null
    var limitMin: String? = null
    var limitMax: String? = null
    var status: String? = null
    var createBy: String? = null
    var createTime: String? = null
    var updateTime: String? = null
    var album: List<String>? = null
    var content: String? = null
    var sku: List<GoodsSkuBean>? = null
    var pv: String? = null
}