package com.common.lib.bean

import java.io.Serializable

class ChainDataBean : Serializable {

    var utgToGb: String? = null  //存储奖励

    var lockNum: String? = null //总锁仓

    var nextElectTime: String? = null //下一个选举高度

    var destrNum: String? = null //销毁数量

    var pledgeNum: String? = null //质押数量

    var utg24: String? = null  //24小时POS产出

    var bandWidthSize: String? = null

    var totalBlockNumber: String? = null  //区块高度

    var ultronNode: String? = null  //总质押
}