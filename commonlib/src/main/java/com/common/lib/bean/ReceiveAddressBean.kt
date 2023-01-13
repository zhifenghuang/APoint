package com.common.lib.bean

import java.io.Serializable

class ReceiveAddressBean : Serializable {
    var id: Int = 0
    var userId: String? = null
    var type: String? = null
    var name: String? = null
    var mobile: String? = null
    var provinceId: String? = null
    var provinceCode: String? = null
    var provinceName: String? = null
    var cityId: String? = null
    var cityCode: String? = null
    var cityName: String? = null
    var districtId: String? = null
    var districtCode: String? = null
    var districtName: String? = null
    var streetId: String? = null
    var streetCode: String? = null
    var streetName: String? = null
    var detail: String? = null
    var tag: String? = null
    var createTime: String? = null
    var districtText: String? = null
    var default: Int = 0
}