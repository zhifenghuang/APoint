package com.common.lib.network

interface HttpListener<Data> {

    fun onSuccess(totalCount: Int, bean: Data?)

    fun dataError(code: Int, msg: String?)

    fun connectError(e: Throwable?)
}