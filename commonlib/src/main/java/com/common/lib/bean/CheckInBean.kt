package com.common.lib.bean

import java.io.Serializable

class CheckInBean : Serializable {
    var userId: String? = null
    var count: Int = 0
    var lastCheckTime: Long = 0
    var createTime: String? = null
    var id: String? = null
    var checkIn: Boolean = false
}