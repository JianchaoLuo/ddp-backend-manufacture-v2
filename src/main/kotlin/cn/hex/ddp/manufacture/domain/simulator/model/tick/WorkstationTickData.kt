package cn.hex.ddp.manufacture.domain.simulator.model.tick

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum

data class WorkstationTickData(
    override val tick: Long,
    val id: Long,
    val mission: Long,
    val name: String,
    val area: AreaEnum,
    val currentAction: String,
    var status: WorkstationStatusEnum
): ITickData
