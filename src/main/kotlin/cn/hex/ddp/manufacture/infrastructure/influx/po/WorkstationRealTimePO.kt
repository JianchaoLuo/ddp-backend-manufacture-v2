package cn.hex.ddp.manufacture.infrastructure.influx.po

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.simulator.model.tick.WorkstationTickData
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum
import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement

@Measurement(name = "workstation_real_time")
class WorkstationRealTimePO(
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
    @Column(name = "status")
    var status: String = ""
) {
    fun toWorkstationTickData(): WorkstationTickData {
        return WorkstationTickData(
            tick = tick,
            id = id.toLong(),
            mission = mission.toLong(),
            name = name,
            area = AreaEnum.valueOf(area),
            status = WorkstationStatusEnum.valueOf(status),
            currentAction = currentAction
        )
    }
}
