package cn.hex.ddp.manufacture.domain.simulator.model.tick

import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.simulator.enums.CarConnectStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.CarLocationStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.FerrySubCarStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarFerryStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarLoadStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarTopRodStatus
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event

abstract class CarTickData(
    override val tick: Long,
    open val id: Long,
    open val mission: Long,
    open val name: String = "",
    open val area: AreaEnum = AreaEnum.DEFAULT,
    open var loadStatus: SubCarLoadStatus = SubCarLoadStatus.EMPTY_LOAD,
    open var topRodStatus: SubCarTopRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN,
    open var ferryStatus: SubCarFerryStatus = SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY,
    open var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    open var currentCoordinate: Coordinate = Coordinate(0.0, 0.0),
    open var targetCoordinate: Coordinate = Coordinate(0.0, 0.0),
    open var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    open var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    open var cargos: List<Any> = listOf(),
    open var currentEvent: Event? = null,
    open var currentRelativePosition: Double = 0.0,
    open var currentAction: String = "",
) : ITickData {
    fun toCarChartData(): CarChartData {
        return CarChartData(
            id = id,
            name = name,
            area = area,
            operationStatus = operationStatus,
            locationStatus = locationStatus,
            connectStatus = connectStatus,
            cargos = cargos
        )
    }
}


data class FerryTickData(
    override val tick: Long,
    override val id: Long,
    override val mission: Long,
    override val name: String,
    override val area: AreaEnum = AreaEnum.DEFAULT,
    override var loadStatus: SubCarLoadStatus = SubCarLoadStatus.EMPTY_LOAD,
    override var topRodStatus: SubCarTopRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN,
    override var ferryStatus: SubCarFerryStatus = SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY,
    override var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    override var currentCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var targetCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    override var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    override var cargos: List<Any> = listOf(),
    override var currentEvent: Event? = null,
    override var currentRelativePosition: Double = 0.0,
    override var currentAction: String = "",
    var subCarStatus: FerrySubCarStatus = FerrySubCarStatus.NOT_CARRY_SUB_CAR,
) : CarTickData(tick, id, mission)

data class SubCarTickData(
    override val tick: Long,
    override val id: Long,
    override val mission: Long,
    override val name: String,
    override val area: AreaEnum = AreaEnum.DEFAULT,
    override var loadStatus: SubCarLoadStatus = SubCarLoadStatus.EMPTY_LOAD,
    override var topRodStatus: SubCarTopRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN,
    override var ferryStatus: SubCarFerryStatus = SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY,
    override var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    override var currentCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var targetCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    override var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    override var cargos: List<Any> = listOf(),
    override var currentEvent: Event? = null,
    override var currentRelativePosition: Double = 0.0,
    override var currentAction: String = "",
) : CarTickData(tick, id, mission)

data class CarChartData(
    val id: Long,
    val name: String,
    val area: AreaEnum = AreaEnum.DEFAULT,
    var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    var cargos: List<Any> = listOf(),
)