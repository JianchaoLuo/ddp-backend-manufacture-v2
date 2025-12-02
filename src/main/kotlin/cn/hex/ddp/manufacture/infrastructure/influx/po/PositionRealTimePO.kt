package cn.hex.ddp.manufacture.infrastructure.influx.po

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum
import cn.hex.ddp.manufacture.domain.simulator.model.tick.PositionTickData
import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement

@Measurement(name = "position_real_time")
data class PositionRealTimePO(
    @Column(name = "tick")
    val tick: Long = 0L,
    @Column(name = "id", tag = true)
    val id: String = "",
    @Column(name = "mission", tag = true)
    val mission: String = "",
    @Column(name = "currentAction")
    val currentAction: String = "",
    @Column(name = "status")
    var status: String=""
) {
    fun toPositionTickData(): PositionTickData {
        return PositionTickData(
            tick = tick,
            id = id.toLong(),
            mission = mission.toLong(),
            status = PositionStatusEnum.valueOf(status),
            currentAction = currentAction
        )
    }
}
