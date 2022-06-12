package com.common.lib.bean

import java.io.Serializable

class StorageBean : Serializable {
    var id: String? = null
    var userId: String? = null
    var address: String? = null
    var blockNumber: String? = null
    var capacity: String? = null
    var bandwidth: String? = null
    var ratio: String? = null
    var pledgeAmount: String? = null
    var pledgeStatus: String? = null
    var vaildNumber: String? = null
    var successNumber: String? = null
    var type: String? = null
    var remark: String? = null
    var recordState: String? = null
    var createBy: String? = null
    var createTime: String? = null
    var updateTime: String? = null
    var status: Int = 0  //状态 0正常 1删除审核中
}