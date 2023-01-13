package com.common.lib.bean

import java.io.Serializable
import java.math.BigDecimal

class DistrictBean : Serializable {

    var text: String? = null
    var value: String? = null
    var pCode: String? = null
    var isSelect = false
    var userId: Int? = 0
}