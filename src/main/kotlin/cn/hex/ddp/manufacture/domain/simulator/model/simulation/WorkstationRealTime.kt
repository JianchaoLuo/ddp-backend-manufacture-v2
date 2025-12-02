package cn.hex.ddp.manufacture.domain.simulator.model.simulation

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum

data class WorkstationRealTime(
    val id: Long,
    val nameEnum: WorkstationNameEnum,
    val name: String,
    var status: WorkstationStatusEnum,
    var currentAction: String = "",
) {
}
