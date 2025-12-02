package cn.hex.ddp.manufacture.domain.simulator.model.simulation

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum
import cn.hex.ddp.manufacture.domain.simulator.enums.CarConnectStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.CarLocationStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.FerrySubCarStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarFerryStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarLoadStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarTopRodStatus
import cn.hex.ddp.manufacture.infrastructure.algorithm.DeployThread
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event
import java.lang.Exception
import java.util.*
import kotlin.math.abs
import kotlin.random.Random


class CarMeta(
    val type: CarTypeEnum,
    val nameEnum: CarNameEnum = CarNameEnum.DEFAULT,
    val name: String = "",
    val emptySpeed: Double = 5.0,
    val fullSpeed: Double = 4.0,
    val raiseTopRodTime: Double = 1.6,
    val fallTopRodTime: Double = 1.6,
    val entryFerryTime: Double = 5.2,
    val leaveFerryTime: Double = 5.2,
    val loadSubCarTime: Double = 5.2,
    val unloadSubCarTime: Double = 5.2,
    val ladeSubCarTime: Double = 5.2,
    val dumpSubCarTime: Double = 5.2,
    val waitTime: Double = 3.0,
    val path: List<Path> = Collections.emptyList<Path>(),
) {
    val currentPath: Path
        get() = path[0]
}

open class CarRealTime(
    open var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    open var currentCoordinate: Coordinate = Coordinate(0.0, 0.0),
    open var targetCoordinate: Coordinate = Coordinate(0.0, 0.0),
    open var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    open var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    open var cargos: List<Any> = listOf(),
    open var currentEvent: Event? = null,
    /**
     * 当前相对位置
     */
    open var currentRelativePosition: Double = 0.0,
    open var currentAction: String = "",
)

abstract class Car(
    open val id: Long,
    open val meta: CarMeta = CarMeta(CarTypeEnum.SUB_CAR),
    open val realTime: CarRealTime = CarRealTime(),
) {
    fun eventFinish(cb: () -> Unit) {
        // 2025-01-08 这里realTime.currentEvent置空可能导致模拟器调用finish方法失败
//        realTime.currentEvent = null
        cb();
    }

    fun eventFinish() {
        // 2025-01-08 这里realTime.currentEvent置空可能导致模拟器调用finish方法失败
//        realTime.currentEvent = null
    }

    fun move(targetCoordinate: Coordinate) {
        val targetX = targetCoordinate.x
        val targetY = targetCoordinate.y

        var currentX = realTime.currentCoordinate.x
        var currentY = realTime.currentCoordinate.y

/*        val target = Coordinate(targetX, targetY)
        val startCoordinate = meta.currentPath.startCoordinate
        val endCoordinate = meta.currentPath.endCoordinate
        if (startCoordinate.x == endCoordinate.x) {
            if (target.x != startCoordinate.x) {
                throw Exception("目标位置不合法")
            }
            if (target.y < min(startCoordinate.y, endCoordinate.y) || target.y > max(
                    startCoordinate.y,
                    endCoordinate.y
                )
            ) {
                throw Exception("目标位置不合法")
            }
        } else if (startCoordinate.y == endCoordinate.y) {
            if (target.y != startCoordinate.y) {
                throw Exception("目标位置不合法")
            }
            if (target.x < min(startCoordinate.x, endCoordinate.x) || target.x > max(
                    startCoordinate.x,
                    endCoordinate.x
                )
            ) {
                throw Exception("目标位置不合法")
            }
        }*/

        // 如果x和y坐标都不在一条线上，说明目标位置不合法
        if (abs(targetX - currentX) >= 0.3 && abs(targetY - currentY) >= 0.3) {
            throw Exception("目标位置不合法")
        }

    }



    override fun toString(): String {
        return "$id $meta $realTime"
    }
}

data class SubCarRealTime(
    override var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    override var currentCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var targetCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    override var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    override var cargos: List<Any> = listOf(),
    override var currentEvent: Event? = null,
    /**
     * 当前相对位置
     */
    override var currentRelativePosition: Double = 0.0,
    override var currentAction: String = "",
    var loadStatus: SubCarLoadStatus = SubCarLoadStatus.EMPTY_LOAD,
    var topRodStatus: SubCarTopRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN,
    var ferryStatus: SubCarFerryStatus = SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY,
    var bindFerry: Long? = null
) : CarRealTime(
    operationStatus,
    currentCoordinate,
    targetCoordinate,
    locationStatus,
    connectStatus,
    cargos,
)

data class FerryRealTime(
    override var operationStatus: CarOperationStatusEnum = CarOperationStatusEnum.STANDBY,
    override var currentCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var targetCoordinate: Coordinate = Coordinate(0.0, 0.0),
    override var locationStatus: CarLocationStatus = CarLocationStatus.ON_TRACK,
    override var connectStatus: CarConnectStatus = CarConnectStatus.ONLINE,
    override var cargos: List<Any> = listOf(),
    override var currentEvent: Event? = null,
    /**
     * 当前相对位置
     */
    override var currentRelativePosition: Double = 0.0,
    override var currentAction: String = "",
    var subCarStatus: FerrySubCarStatus = FerrySubCarStatus.NOT_CARRY_SUB_CAR,
    var bindCar: Long? = null
) : CarRealTime(
    operationStatus,
    currentCoordinate,
    targetCoordinate,
    locationStatus,
    connectStatus,
    cargos
)

data class SubCar(
    override val id: Long,
    override var realTime: SubCarRealTime = SubCarRealTime(),
    override val meta: CarMeta = CarMeta(CarTypeEnum.SUB_CAR)
) : Car(id, meta, realTime) {
    fun topRodRaise(timer: Timer, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_IN_RAISING
        val subCar = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                subCar.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_RAISED
                subCar.realTime.loadStatus = SubCarLoadStatus.FULL_LOAD
                // todo: 如果有点位，需要更新点位状态，从占有变为未占用
                // TODO：如果有工岗，需要更新工岗状态，从带搬运变为空闲
                // TODO：如果有设备，更改设备状态，工作完成改为待机
                deployThread.eventFinish(subCar.realTime.currentEvent)
                subCar.eventFinish {
                    print("$subCar finish")
                }
            }
        }, (1000 * (this.meta.raiseTopRodTime + noise)).toLong())
    }

    fun topRodDown(timer: Timer, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_IN_FALL_DOWN
        val subCar = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                subCar.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN
                subCar.realTime.loadStatus = SubCarLoadStatus.EMPTY_LOAD
                // todo: 如果有点位，需要更新点位状态，从未占用变为占用
                // TODO：如果有工岗，需要更新工岗状态，1.如果是造型区或合箱区：如果工岗当前空闲，改为待配对；如果当前待配对，改为工作中
                // 2.如果是浇筑区或开箱区：如果当前为空闲，改为工作中
                // TODO：如果有设备，更改设备状态，从待机改为工作中
                deployThread.eventFinish(subCar.realTime.currentEvent)
                subCar.eventFinish()
            }
        }, (1000 * (this.meta.fallTopRodTime + noise)).toLong())
    }

    fun enterFerry(timer: Timer, event: Event, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.ferryStatus = SubCarFerryStatus.ENTRY_FERRY
        val subCar = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                subCar.realTime.ferryStatus = SubCarFerryStatus.SUB_CAR_IN_FERRY
                deployThread.eventFinish(subCar.realTime.currentEvent)
                subCar.eventFinish()
            }
        }, (1000 * (this.meta.entryFerryTime + noise)).toLong())
    }

    fun leaveFerry(timer: Timer,  event: Event, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.ferryStatus = SubCarFerryStatus.GET_OFF_FERRY
        val subCar = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                subCar.realTime.ferryStatus = SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY
                deployThread.eventFinish(subCar.realTime.currentEvent)
                subCar.eventFinish()
            }
        }, (1000 * (this.meta.leaveFerryTime + noise)).toLong())
    }

    fun load(timer: Timer,  event: Event, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.loadStatus = SubCarLoadStatus.EMPTY_LOAD
        val subCar = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                subCar.realTime.loadStatus = SubCarLoadStatus.FULL_LOAD
                deployThread.eventFinish(subCar.realTime.currentEvent)
                subCar.eventFinish()
            }
        }, (1000 * (this.meta.unloadSubCarTime + noise)).toLong())
    }

    fun unload(timer: Timer, event: Event, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.loadStatus = SubCarLoadStatus.FULL_LOAD
        val subCar = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                subCar.realTime.loadStatus = SubCarLoadStatus.EMPTY_LOAD
                deployThread.eventFinish(subCar.realTime.currentEvent)
                subCar.eventFinish()
            }
        }, (1000 * (this.meta.unloadSubCarTime + noise)).toLong())
    }
}

data class Ferry(
    override val id: Long,
    override val realTime: FerryRealTime = FerryRealTime(),
    override val meta: CarMeta = CarMeta(CarTypeEnum.FERRY_CAR)
) : Car(id, meta, realTime) {

    fun ladeSubCar(timer: Timer,  event: Event, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.subCarStatus = FerrySubCarStatus.ENTRY_FERRY
        val ferry = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                ferry.realTime.subCarStatus = FerrySubCarStatus.CARRY_SUB_CAR
                deployThread.eventFinish(ferry.realTime.currentEvent)
                ferry.eventFinish()
            }
        }, (1000 * (this.meta.ladeSubCarTime + noise)).toLong())
    }

    fun dumpSubCar(timer: Timer,  event: Event, deployThread: DeployThread) {
        val noise = Random.nextDouble(-3.0, 3.0)
        this.realTime.subCarStatus = FerrySubCarStatus.GET_OFF_FERRY
        val ferry = this
        timer.schedule(object : TimerTask() {
            override fun run() {
                ferry.realTime.subCarStatus = FerrySubCarStatus.NOT_CARRY_SUB_CAR
                deployThread.eventFinish(ferry.realTime.currentEvent)
                ferry.eventFinish()
            }
        }, (1000 * (this.meta.dumpSubCarTime + noise)).toLong())
    }
}
