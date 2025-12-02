package cn.hex.ddp.manufacture.infrastructure.influx.po

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum
import cn.hex.ddp.manufacture.domain.simulator.model.tick.EquipmentTickData
import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement

@Measurement(name = "equipment_real_time")
data class EquipmentRealTimePO(
    @Column(name = "tick")
    val tick: Long = 0L,
    @Column(name = "id", tag = true)
    val id: String = "",
    @Column(name = "mission", tag = true)
    val mission: String = "",
    @Column(name = "name")
    val name: String = "",
    @Column(name = "area")
    val area: String = "",
    @Column(name = "currentAction")
    val currentAction: String = "",
    @Column(name = "operationStatus")
    var operationStatus: String = "",
    @Column(name = "moltenIronQuantity")
    var moltenIronQuantity: Double = 0.0
) {
    fun toEquipmentTickData(): EquipmentTickData {
        return EquipmentTickData(
            tick = tick,
            id = id.toLong(),
            mission = mission.toLong(),
            name = name,
            area = AreaEnum.valueOf(area),
            operationStatus = EquipmentOperationStatusEnum.valueOf(operationStatus),
            currentAction = currentAction,
            moltenIronQuantity = moltenIronQuantity
        )
    }
}

