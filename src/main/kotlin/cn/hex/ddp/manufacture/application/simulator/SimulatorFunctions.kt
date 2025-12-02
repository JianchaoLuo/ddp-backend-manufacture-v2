package cn.hex.ddp.manufacture.application.simulator

import cn.hex.ddp.manufacture.application.simulator.processor.CommandProcessor
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum
import cn.hex.ddp.manufacture.domain.simulator.enums.FerrySubCarStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarFerryStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarLoadStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarTopRodStatus
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * 模拟器中下发事件等具体改变数据的方法实现
 *
 * @author Huhaisen
 * @date 2024/10/14
 */
class SimulatorFunctions(
    private val commandProcessor: CommandProcessor,
) {
    private val timer: Timer = Timer()

    inner class CarFunctions {
        /**
         * 车辆下发顶杆升指令
         */
        fun risingInstruction(car: Car, event: Event) {
            car as SubCar
            val noise = Random.nextDouble(-1.0, 1.0)
            car.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_IN_RAISING
            car.realTime.operationStatus = CarOperationStatusEnum.WORKING
            timer.schedule(object : TimerTask() {
                override fun run() {
                    car.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_RAISED
                    car.realTime.loadStatus = SubCarLoadStatus.FULL_LOAD
                    car.realTime.operationStatus = CarOperationStatusEnum.STOPPING
                    // 如果有点位，需要更新点位状态，从占有变为未占用
                    val positionId = event.startPositionEvent?.positionId
                    if (positionId != null) {
                        val position = commandProcessor.getPosition(positionId)
                        position.status = PositionStatusEnum.UNOCCUPIED
                    }
                    // 如果有工岗，需要更新工岗状态，从带搬运变为空闲
                    val workstationId = event.startWorkStationEvent?.id
                    if (workstationId != null) {
                        val workstation = commandProcessor.getWorkstation(workstationId)
                        // 2025-01-08 逻辑更新，开箱区无需特殊处理
/*                        // 如果是开箱区，开完箱后工岗上可能有多个物件带搬运，因此其状态根据事件中的工岗结束状态来判断
                        if (workstation.nameEnum.area == AreaEnum.SOUTH_OPEN_BOX) {
                            workstation.status = event.endWorkStationEvent.workStationStatus.first()
                                ?: WorkstationStatusEnum.FREE
                        } else {
                            workstation.status = WorkstationStatusEnum.FREE
                        }*/
                        workstation.status = WorkstationStatusEnum.FREE
                    }
                    // 如果有设备，更改设备状态，工作完成改为待机
                    val equipmentId = event.startEquipmentEvent?.id
                    if (equipmentId != null) {
                        val equipment = commandProcessor.getEquipment(equipmentId)
                        equipment.realTime.operationStatus = EquipmentOperationStatusEnum.STANDBY
                    }
                    commandProcessor.eventFinish(event)
                    car.eventFinish {
                        println("$car finish")
                    }
                }
            }, (1000 * (car.meta.raiseTopRodTime + noise)).toLong())
        }

        /**
         * 车辆下发顶杆降指令
         */
        fun landingInstruction(car: Car, event: Event) {
            car as SubCar
            val noise = Random.nextDouble(-1.0, 1.0)
            car.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_IN_FALL_DOWN
            car.realTime.operationStatus = CarOperationStatusEnum.WORKING
            timer.schedule(object : TimerTask() {
                override fun run() {
                    car.realTime.topRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN
                    car.realTime.loadStatus = SubCarLoadStatus.EMPTY_LOAD
                    car.realTime.operationStatus = CarOperationStatusEnum.STOPPING
                    // 如果有点位，需要更新点位状态，从未占用变为占用
                    val positionId = event.startPositionEvent?.positionId
                    if (positionId != null) {
                        val position = commandProcessor.getPosition(positionId)
                        position.status = PositionStatusEnum.OCCUPIED
                    }
                    /**
                     *  如果有工岗，需要更新工岗状态，
                     *  1.如果是造型区或合箱区：如果工岗当前空闲，改为待配对；如果当前待配对，改为工作中
                     *  2.如果是浇筑区或开箱区：如果当前为空闲，改为工作中
                     */
                    val workstationId = event.startWorkStationEvent?.id
                    if (workstationId != null) {
                        val workstation = commandProcessor.getWorkstation(workstationId)
                        when (workstation.nameEnum.area) {
                            AreaEnum.SOUTH_MOULDING, AreaEnum.SOUTH_COMBINE_BOX -> {
                                if (workstation.status == WorkstationStatusEnum.FREE) {
                                    workstation.status = WorkstationStatusEnum.WAIT_PAIR
                                } else if (workstation.status == WorkstationStatusEnum.WAIT_PAIR) {
                                    workstation.status = WorkstationStatusEnum.WORKING
                                }
                            }

                            AreaEnum.SOUTH_POURING, AreaEnum.SOUTH_OPEN_BOX -> {
                                if (workstation.status == WorkstationStatusEnum.FREE) {
                                    workstation.status = WorkstationStatusEnum.WORKING
                                }
                            }

                            else -> {}
                        }
                    }
                    // 如果有设备，更改设备状态，从待机改为工作中
                    val equipmentId = event.startEquipmentEvent?.id
                    if (equipmentId != null) {
                        val equipment = commandProcessor.getEquipment(equipmentId)
                        equipment.realTime.operationStatus = EquipmentOperationStatusEnum.WORKING
                    }
                    commandProcessor.eventFinish(event)
                    car.eventFinish()
                }
            }, (1000 * (car.meta.fallTopRodTime + noise)).toLong())
        }

        /**
         * 车辆下发等待指令
         */
        fun waitInstruction(car: Car, event: Event) {
            car.realTime.operationStatus = CarOperationStatusEnum.STOPPING
            timer.schedule(object : TimerTask() {
                override fun run() {
                    car.realTime.operationStatus = CarOperationStatusEnum.STOPPING
                    commandProcessor.eventFinish(event)
                    car.eventFinish()
                }
            }, (1000 * car.meta.waitTime).toLong())
        }

        fun updateCar(car: Car) {
            if (car.realTime.currentEvent == null) {
                return
            }
            // 通过事件中的是否完成表示判断是否继续执行事件，若为true表示已完成，不需再执行  2025-1-8
            if (car.realTime.currentEvent?.isCompleteFlag == true) {
                return
            }

//            if (car.id == 4L) {
//                println("====####====车辆 4L 有当前事件,指令: ${car.realTime.currentEvent!!.instruction}")
//                println("====####====车辆 4L 当前坐标: ${car.realTime.currentCoordinate}")
//            }
            val instruction = car.realTime.currentEvent!!.instruction
            if (InstructionsEnum.RUNNING == instruction.instructions) {
                // 车辆开始移动，操作状态改为工作中
                car.realTime.operationStatus = CarOperationStatusEnum.WORKING
                val targetX = instruction.runAimCoordinate.x
                val targetY = instruction.runAimCoordinate.y
                val noise = 2 * (Random.nextFloat() - 0.5)
                // 根据车辆当前载物情况，获取车辆移动速度
                var speed = 5.0
                if (car is SubCar) {
                    speed = if (car.realTime.loadStatus == SubCarLoadStatus.EMPTY_LOAD) {
                        car.meta.emptySpeed
                    } else {
                        car.meta.fullSpeed
                    }
                } else if (car is Ferry) {
                    speed = if (car.realTime.subCarStatus == FerrySubCarStatus.NOT_CARRY_SUB_CAR) {
                        car.meta.emptySpeed
                    } else {
                        car.meta.fullSpeed
                    }
                }

/*                val direction = car.meta.currentPath.direction

                if (direction == RailDirectionEnum.BOTTOM_TO_TOP || direction == RailDirectionEnum.TOP_TO_BOTTOM) {
                    if (car.realTime.currentCoordinate.y < targetY) {
                        car.realTime.currentCoordinate.y = min(
                            car.meta.speed + car.realTime.currentCoordinate.y + noise, targetY
                        )
                    } else {
                        car.realTime.currentCoordinate.y = max(
                            car.realTime.currentCoordinate.y - car.meta.speed + noise, targetY
                        )
                    }
                    if (car.realTime.currentCoordinate.y == targetY) {
                        this.deployThread?.eventFinish(car.realTime.currentEvent)
                        car.eventFinish()
                    }
                } else {
                    if (car.realTime.currentCoordinate.x < targetX) {
                        car.realTime.currentCoordinate.x = min(
                            car.meta.speed + car.realTime.currentCoordinate.x + noise, targetX
                        )
                    } else {
                        car.realTime.currentCoordinate.x = max(
                            car.realTime.currentCoordinate.x - car.meta.speed + noise, targetX
                        )
                    }
                    if (car.realTime.currentCoordinate.x == targetX) {
                        this.deployThread?.eventFinish(car.realTime.currentEvent)
                        car.eventFinish()
                    }
                }*/

                // 如果x相同，则移动y轴
                if (abs(targetX - car.realTime.currentCoordinate.x) < 0.3) {
                    if (car.realTime.currentCoordinate.y < targetY) {
                        car.realTime.currentCoordinate.y = min(
                            speed + car.realTime.currentCoordinate.y + noise, targetY
                        )
                    } else {
                        car.realTime.currentCoordinate.y = max(
                            car.realTime.currentCoordinate.y - speed + noise, targetY
                        )
                    }
                    updateLocationAndStatus(car)
//                log.debug("【车辆所属事件ID: ${car.realTime.currentEvent!!.id} 】")
                    if (abs(car.realTime.currentCoordinate.y - targetY) < 0.3) {
                        carFinishRun(car, targetY, targetX)
                    }
//                log.debug("【纵向移动】车辆 ${car.id} 当前坐标：${car.realTime.currentCoordinate}")
                } else if (abs(targetY - car.realTime.currentCoordinate.y) < 0.3) {
                    // 如果y相同，则移动x轴
                    if (car.realTime.currentCoordinate.x < targetX) {
                        car.realTime.currentCoordinate.x = min(
                            speed + car.realTime.currentCoordinate.x + noise, targetX
                        )
                    } else {
                        car.realTime.currentCoordinate.x = max(
                            car.realTime.currentCoordinate.x - speed + noise, targetX
                        )
                    }
                    updateLocationAndStatus(car)
//                log.debug("【车辆所属事件ID: ${car.realTime.currentEvent!!.id} 】")
                    if (abs(car.realTime.currentCoordinate.x - targetX) < 0.3) {
                        carFinishRun(car, targetY, targetX)
                    }
//                log.debug("【横向移动】车辆 ${car.id} 当前坐标：${car.realTime.currentCoordinate}")
                }
            }
        }

        /**
         * 车辆运行结束后，更新坐标和状态
         */
        private fun carFinishRun(
            car: Car,
            targetY: Double,
            targetX: Double
        ) {
            // 车辆运行结束, 操作状态改为停止
            car.realTime.operationStatus = CarOperationStatusEnum.STOPPING
            car.realTime.currentCoordinate.y = targetY
            car.realTime.currentCoordinate.x = targetX
            // 为了确保子母车位置完全一致，结束前再执行一次updateLocationAndStatus()
            updateLocationAndStatus(car)
            commandProcessor.eventFinish(car.realTime.currentEvent!!)
            log.debug("【车辆移动调用carFinishRun】 事件：{} + {} + {}", car.realTime.currentEvent!!.id,
                car.realTime.currentEvent!!.eventName, car.realTime.currentEvent!!.instruction)
            car.eventFinish()
        }

        /**
         * 同步更新所绑定子车位置以及子母车状态信息
         * 1. 其中更新所绑定子车位置信息，需要判断子车是否在母车上，如果在母车上，则子车的坐标应该与母车同步
         * 2. 更新子母车状态信息，需要判断子母车当前坐标是否相同，分情况更新为不同的状态
         *
         * @param [car]
         */
        private fun updateLocationAndStatus(car: Car) {
            // 如果母车上绑定了子车且子车状态为在母车上，那么子车的坐标应该与母车同步
            if (car is Ferry && car.realTime.bindCar != null) {
                val subCar = commandProcessor.getCar(car.realTime.bindCar!!) as SubCar
                if (subCar.realTime.ferryStatus == SubCarFerryStatus.SUB_CAR_IN_FERRY) {
                    subCar.realTime.currentCoordinate = Coordinate(
                        car.realTime.currentCoordinate.x,
                        car.realTime.currentCoordinate.y
                    )
                }
            }

            // 如果是绑定了母车的子车，需要根据子车与母车位置，更新子母车状态
            if (car is SubCar && car.realTime.bindFerry != null) {
                val ferry = commandProcessor.getCar(car.realTime.bindFerry!!) as Ferry
                // 如果子车与母车坐标相同，则子车状态为"子车完全在母车上", 母车状态为"母车搭载子车"
                if (abs(car.realTime.currentCoordinate.x - ferry.realTime.currentCoordinate.x) < 0.3 &&
                    abs(car.realTime.currentCoordinate.y - ferry.realTime.currentCoordinate.y) < 0.3
                ) {
                    car.realTime.ferryStatus = SubCarFerryStatus.SUB_CAR_IN_FERRY
                    ferry.realTime.subCarStatus = FerrySubCarStatus.CARRY_SUB_CAR
                } else if (abs(car.realTime.currentCoordinate.x - ferry.realTime.currentCoordinate.x) >= 0.3 ||
                    abs(car.realTime.currentCoordinate.y - ferry.realTime.currentCoordinate.y) >= 0.3
                ) {
                    // 如果子车与母车坐标不同，则子车状态为"子车不在母车上", 母车状态为"母车未搭载子车"
                    car.realTime.ferryStatus = SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY
                    ferry.realTime.subCarStatus = FerrySubCarStatus.NOT_CARRY_SUB_CAR
                }
            }
        }
    }

    inner class EquipmentFunctions {
        /**
         * 设备下发通知指令
         */
        fun equipmentNotice(equipment: Equipment, event: Event) {
            val noise = Random.nextDouble(-1.0, 1.0)
            equipment.realTime.operationStatus = EquipmentOperationStatusEnum.WORKING
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        equipment.realTime.operationStatus = EquipmentOperationStatusEnum.FINISHED
                        commandProcessor.eventFinish(event)
                        equipment.eventFinish()
                    }
                },
                ((equipment.meta.config.processTime + noise) * 1000).toLong()
            )
        }

        /**
         * 设备使用铁水
         */
        fun useMoltenIron(equipment: Equipment, event: Event) {
            val noise = Random.nextDouble(-1.0, 1.0)
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        // TODO: 每次减少的铁水量需要从Event中获取, 暂时用随机数模拟
                        equipment.realTime.moltenIronQuantity = BigDecimal(
                            equipment.realTime.moltenIronQuantity - Random.nextDouble(0.3, 0.6))
                            .setScale(3, RoundingMode.HALF_UP)
                            .toDouble()
                        commandProcessor.eventFinish(event)
                        equipment.eventFinish()
                    }
                },
                ((equipment.meta.config.processTime + noise) * 1000).toLong()
            )
        }

        /**
         * 准备铁水
         */
        fun prepareMoltenIron(equipment: Equipment, event: Event) {
            val noise = Random.nextDouble(-1.0, 1.0)
            equipment.realTime.operationStatus = EquipmentOperationStatusEnum.WORKING
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        equipment.realTime.moltenIronQuantity = event.instruction.moltenIronQuantity
                        equipment.realTime.operationStatus = EquipmentOperationStatusEnum.STANDBY
                        commandProcessor.eventFinish(event)
                        equipment.eventFinish()
                    }
                },
                ((equipment.meta.config.electricFurnaceFiringTime + noise) * 1000).toLong()
            )
        }
    }

    inner class WorkstationFunctions {
        /**
         * 工岗下发通知指令
         */
        fun workstationNotice(workstation: WorkstationRealTime, event: Event) {
            val noise = Random.nextDouble(3.0, 5.0)
            workstation.status = WorkstationStatusEnum.WORKING
            timer.schedule(object : TimerTask() {
                override fun run() {
                    workstation.status = WorkstationStatusEnum.WAIT_TRANSPORT
                    commandProcessor.eventFinish(event)
                }
            }, (1000 * noise).toLong())
        }
    }

    inner class PositionFunctions {
        /**
         * 点位下发通知指令
         */
        fun positionNotice(position: PositionRealTime, event: Event) {
            val noise = Random.nextDouble(2.0, 3.0)
            timer.schedule(object : TimerTask() {
                override fun run() {
                    when (position.nameEnum.area) {
                        // 如果是冷却区的点位，则将其状态变为完成冷却 FINISH_COOLING
                        AreaEnum.SOUTH_COOLING -> {
                            position.status = PositionStatusEnum.FINISH_COOLING
                        }
                        // 如果是开箱区或造型区的点位，则将其状态变为已占有 OCCUPIED
                        AreaEnum.SOUTH_OPEN_BOX, AreaEnum.SOUTH_MOULDING -> {
                            // 【特殊情况-模拟工厂控制】如果是铸件回流传输点位，则将其状态变为未占用  或者
                            // 【特殊情况-模拟工厂控制】如果是砂箱回流行车放置点，则将其状态变为未占用
                            if (position.nameEnum == PositionNameEnum.CASTING_REFLUX_SHIPPING_POSITION ||
                                position.nameEnum == PositionNameEnum.SAND_BOX_ROW_CAR_POSITION) {
                                position.status = PositionStatusEnum.UNOCCUPIED
                            } else {
                                position.status = PositionStatusEnum.OCCUPIED
                            }
                        }
                        else -> {}
                    }
                    commandProcessor.eventFinish(event)
                }
            }, (1000 * noise).toLong())
        }
    }
}