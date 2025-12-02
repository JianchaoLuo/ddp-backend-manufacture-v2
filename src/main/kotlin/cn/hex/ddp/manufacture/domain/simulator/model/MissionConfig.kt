package cn.hex.ddp.manufacture.domain.simulator.model

import cn.hex.ddp.manufacture.domain.simulator.enums.SimulateTypeEnum
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*
import cn.hex.ddp.manufacture.domain.task.model.Fault
import java.time.LocalDateTime

// todo 把position,workstation,path也改造成realtime和meta分离
data class MissionConfig(
    val missionId: Long,
    val startTime: LocalDateTime,
    val cars: List<Car>,
    val equipments: List<Equipment>,
    val positions: List<PositionRealTime>,
    val workstations: List<WorkstationRealTime>,
    val paths: List<Path>,
    val saveToDB: Boolean = true,
    val simulateType: SimulateTypeEnum,
    val faults: List<Fault>? = null // 新增故障列表
)
