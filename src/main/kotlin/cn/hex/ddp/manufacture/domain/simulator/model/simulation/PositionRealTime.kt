package cn.hex.ddp.manufacture.domain.simulator.model.simulation

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum


data class PositionRealTime(
    val id: Long,
    var status: PositionStatusEnum = PositionStatusEnum.OCCUPIED,
    var currentAction: String = "",
    val nameEnum: PositionNameEnum
)
