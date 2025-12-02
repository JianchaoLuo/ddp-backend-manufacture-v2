package cn.hex.ddp.manufacture.domain.simulator.model.tick

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum

data class PositionTickData(
    override val tick: Long,
    val id: Long,
    val mission: Long,
    val currentAction: String,
    var status: PositionStatusEnum
) : ITickData

