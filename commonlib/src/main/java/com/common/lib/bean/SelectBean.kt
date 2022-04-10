package com.common.lib.bean

import java.io.Serializable

class SelectBean(text: String?, isSelect: Boolean) : Serializable {
    var text: String? = null
    var isSelect: Boolean = false

    init {
        this.text = text
        this.isSelect = isSelect
    }
}