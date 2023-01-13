package com.common.lib.bean

import java.io.Serializable

class GoodsSkuBean : Serializable {
    var id: Int = 0
    var sku: String? = null
    var skuId: String? = null
    var goodsId: String? = null
    var stockNum: String? = null
    var marketPrice: String? = null
    var salePrice: String? = null
    var createTime: String? = null

    var isSelect = false
}