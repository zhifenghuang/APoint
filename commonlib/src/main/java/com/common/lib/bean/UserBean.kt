package com.common.lib.bean

import java.io.Serializable

class UserBean : Serializable {

    var userId: String? = null

    var loginAccount: String? = null

    var status: Int = 0


    var token: String? = null

    var nickName: String? = null

    var avatarUrl: String? = null

    var endTime: String? = null

    var mobile: String? = null

    var email: String? = null

    var createTime: String? = null

    var refereeCount: Int? = 0

    var completeCount: String? = null

    var authStatus: Boolean = false

    var paymentStatus: Boolean = false

    var code: String? = null

    var gradeId: Int = 0
    var grade: GradeBean? = null
    var agentId: Int = 0
    var agent: GradeBean? = null

    var agentTitle: String? = null
}