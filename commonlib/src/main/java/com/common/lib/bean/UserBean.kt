package com.common.lib.bean

import java.io.Serializable

class UserBean : Serializable {

    var userId: String? = null

    var loginAccount: String? = null

    var status: Int = 0

    var hash: String? = null

    var token: String? = null

    var fullName: String? = null

    var avatarUrl: String? = null

    var endTime: String? = null

    var mobile: String? = null

    var email: String? = null

    var createTime: String? = null

    var authStatus: Boolean = false

    var paymentStatus: Boolean = false

    var code: String? = null

    var grade: GradeBean? = null

}