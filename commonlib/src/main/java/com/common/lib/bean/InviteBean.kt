package com.common.lib.bean

import java.io.Serializable

class InviteBean : Serializable {
    var userId: String? = null

    var id: String? = null

    var refereeAmount: String? = null

    var totalAmount: String? = null

    var sharingAmount: String? = null

    var agentAmount: String? = null

    var leftAmount: String? = null  //左1区业绩
    var left2Amount: String? = null//左2区业绩
    var rightAmount: String? = null//其它区业绩
    var teamAmount: String? = null//团队业绩

    var createTime: String? = null

    var gradeId: Int = 0
}