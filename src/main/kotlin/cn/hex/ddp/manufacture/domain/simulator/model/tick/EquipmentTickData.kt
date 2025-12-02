package cn.hex.ddp.manufacture.domain.simulator.model.tick

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum

data class EquipmentTickData(
    override val tick: Long,
    val id: Long,
    val mission: Long,
    val name: String,
    val area: AreaEnum,
    val currentAction: String,
    var operationStatus: EquipmentOperationStatusEnum,
    var moltenIronQuantity: Double
) : ITickData
