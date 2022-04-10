package com.common.lib.bean

import java.io.Serializable

class ChainBlockBean : Serializable {

    var hash: String? = null

    var blockNumber: String? = null

    var isTrunk: String? = null

    var timeStamp: Long? = null

    var minerAddress: String? = null

    var blockSize: String? = null

    var gasLimit: String? = null

    var gasUsed: String? = null

    var reward: String? = null

    var txsCount: String? = null

    var nonce: String? = null

    var difficulty: String? = null

    var totalDifficulty: String? = null

    var parentHash: String? = null

    var avgGasPrice: String? = null

    var blockTransactionCount: String? = null

    var lockamount: String? = null

    var releaseamount: String? = null
}