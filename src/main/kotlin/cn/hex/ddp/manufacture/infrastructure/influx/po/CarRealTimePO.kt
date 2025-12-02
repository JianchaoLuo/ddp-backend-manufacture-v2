package cn.hex.ddp.manufacture.infrastructure.influx.po

import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.simulator.model.tick.CarTickData
import cn.hex.ddp.manufacture.domain.simulator.model.tick.FerryTickData
import cn.hex.ddp.manufacture.domain.simulator.model.tick.SubCarTickData
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate
import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement

@Measurement(name = "car_real_time")
data class CarRealTimePO(
    @Column(name = "tick")
    val tick: Long = 0L,
    @Column(name = "id", tag = true)
    val id: String = "",
    @Column(name = "mission", tag = true)
    val mission: String = "",
    @Column(name = "type", tag = true)
    val type: String = "",
    @Column(name = "name")
    val name: String = "",
    @Column(name = "area")
    val area: String = "",
    @Column(name = "currentCoordinate")
    val currentCoordinate: String = "",
    @Column(name = "targetCoordinate")
    val targetCoordinate: String = "",
    @Column(name = "operationStatus")
    val operationStatus: String = "",
    @Column(name = "currentAction")
    val currentAction: String = "",
) {
    fun toCarTickData(): CarTickData {
        val typeEnum = CarTypeEnum.valueOf(type)
        if (CarTypeEnum.FERRY_CAR == typeEnum) {
            return FerryTickData(
                tick = tick,
                id = id.toLong(),
                mission = mission.toLong(),
                name = name,
                area = AreaEnum.valueOf(area),
                operationStatus = CarOperationStatusEnum.valueOf(operationStatus),
                currentCoordinate = Coordinate.fromStr(currentCoordinate),
//                targetCoordinate = Coordinate.fromStr(targetCoordinate),
                currentAction = currentAction
            )
        } else {
            return SubCarTickData(
                tick = tick,
                id = id.toLong(),
                mission = mission.toLong(),
                name = name,
                area = AreaEnum.valueOf(area),
                operationStatus = CarOperationStatusEnum.valueOf(operationStatus),
                currentCoordinate = Coordinate.fromStr(currentCoordinate),
//                targetCoordinate = Coordinate.fromStr(targetCoordinate),
                currentAction = currentAction
            )
        }

    }
}
