package cn.hex.ddp.manufacture.application.simulator.processor

import cn.hex.ddp.manufacture.application.simulator.SimulatorFunctions
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum
import cn.hex.ddp.manufacture.domain.simulator.enums.SimulateTypeEnum
import cn.hex.ddp.manufacture.domain.simulator.model.MissionConfig
import cn.hex.ddp.manufacture.domain.simulator.model.cache.SimulateCache
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum
import cn.hex.ddp.manufacture.domain.task.enums.SimulateStatusEnum
import cn.hex.ddp.manufacture.domain.task.manager.TaskManager
import cn.hex.ddp.manufacture.domain.task.model.Fault
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.DeployThread
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event
import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil
import cn.hex.ddp.manufacture.infrastructure.common.spring.SpringContextUtil
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.influx.repository.InfluxRepository
import cn.hex.ddp.manufacture.infrastructure.plcmodule.PLCClient
import cn.hex.ddp.manufacture.infrastructure.plcmodule.simulator.PLCSimulator
import cn.hutool.extra.spring.SpringUtil
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.abs

// todo 把外界获取信息和内部更新逻辑分开
class CommandProcessor(
    private val config: MissionConfig,
) : Thread() {
    private var currentTime: Instant = config.startTime.toInstant(ZoneOffset.ofHours(8))
    private val timeUnit = 1L
    private var shouldStop = false
    private var deployThread: DeployThread? = null
    private var plcSimulator: PLCSimulator? = null
    private var simulatorFunctions: SimulatorFunctions = SimulatorFunctions(this)
    private var plcClient: PLCClient = PLCClient(this)

    private val timer: Timer = Timer()
    private val cars = config.cars
    private val carMap = config.cars.associate { it.id to it }
    private val equipmentMap = config.equipments.associate { it.id to it }
    private val equipments = config.equipments
    private val positionMap = config.positions.associate { it.id to it }
    private val workstationMap = config.workstations.associate { it.id to it }
    private val pathMap = config.paths.associate { it.id to it }
    private val productMap = HashMap<Long, Product>()

    // 故障与原始状态缓存
    private val faults: List<Fault> = config.faults ?: emptyList()
    private val carOriginalStatus: MutableMap<Long, CarOperationStatusEnum> = mutableMapOf()
    private val equipmentOriginalStatus: MutableMap<Long, EquipmentOperationStatusEnum> = mutableMapOf()
    private val workstationOriginalStatus: MutableMap<Long, WorkstationStatusEnum> = mutableMapOf()

    @Volatile
    @set:Synchronized
    private var currentTick = 0L
    private val timeDifference = Duration.between(config.startTime, LocalDateTime.now())
    private val influxRepository: InfluxRepository = SpringUtil.getBean(InfluxRepository::class.java)
    private val taskManager: TaskManager = SpringContextUtil.getBean(TaskManager::class.java)
    fun setDeployThread(deployThread: DeployThread) {
        this.deployThread = deployThread
    }

    fun setPLCSimulator(plcSimulator: PLCSimulator) {
        this.plcSimulator = plcSimulator
    }

    fun go() {
        assert(deployThread != null)
    }

    override fun run() {
        if (config.simulateType == SimulateTypeEnum.OFF_LINE) {
            // 删除influxdb中已存在的仿真数据
            deleteDataIfExists()
        }
        while (SimulateCache.running) {
            if (shouldStop) {
                break
            }
            // 每 tick 执行故障调度（根据 currentTick 切换资源状态）
            processFaultsForCurrentTick()

            if (config.simulateType == SimulateTypeEnum.OFF_LINE) {
                cars.stream().parallel().forEach {
                    this.simulatorFunctions.CarFunctions().updateCar(it)
                }
                // 每次都更新一下仿真任务表中的最大帧 TODO：打标记记录仿真进度，实时更新
                updateTaskInfo(SimulateStatusEnum.PROGRESSING, 10F)
            }
//            currentTime.plusSeconds(timeUnit)
            sleep(500)
            recordTick()
//            SimulateCache.running = false
        }
        SimulateCache.T5 = LocalDateTime.now()
    }

    // 每 tick 执行的故障调度
    private fun processFaultsForCurrentTick() {
        log.info("处理故障调度，当前 tick：{}", currentTick);
        if (faults.isEmpty()) return

        // 计算当前 tick 激活的故障
        val activeFaults = faults.filter { it.isInFaultPeriod(currentTick, config.startTime) }

        // 处于故障中的资源集合
        val activeCarIds: Set<Long> = activeFaults
            .filter { it.resourceType == ResourceTypeEnum.CAR }
            .map { it.resourceId }
            .toSet()

        val activeEquipmentIds: Set<Long> = activeFaults
            .filter { it.resourceType == ResourceTypeEnum.EQUIPMENT }
            .map { it.resourceId }
            .toSet()

        //  工岗进入故障，保存原状态并置为 UNAVAILABLE
        val activeWorkstationIds: Set<Long> = activeFaults
            .filter { it.resourceType == ResourceTypeEnum.WORKSTATION }
            .map { it.resourceId }
            .toSet()

        // 车辆进入故障：保存原状态并置为 BREAKDOWN（子车/摆渡车统一处理）
        activeCarIds.forEach { carId ->
            val car = carMap[carId] ?: return@forEach
            if (!carOriginalStatus.containsKey(carId)) {
                carOriginalStatus[carId] = car.realTime.operationStatus
            }
            if (car.realTime.operationStatus != CarOperationStatusEnum.BREAKDOWN) {
                car.realTime.operationStatus = CarOperationStatusEnum.BREAKDOWN
                log.debug("故障生效(车): id={}, tick={}", carId, currentTick)
            }
        }

        //  设备进入故障：保存原状态并置为 BREAKDOWN
        activeEquipmentIds.forEach { eqId ->
            val eq = equipmentMap[eqId] ?: return@forEach
            if (!equipmentOriginalStatus.containsKey(eqId)) {
                equipmentOriginalStatus[eqId] = eq.realTime.operationStatus
            }
            if (eq.realTime.operationStatus != EquipmentOperationStatusEnum.BREAKDOWN) {
                eq.realTime.operationStatus = EquipmentOperationStatusEnum.BREAKDOWN
                log.debug("故障生效(设备): id={}, tick={}", eqId, currentTick)
            }
        }

        //  工岗进入故障，保存原状态并置为 UNAVAILABLE
        activeWorkstationIds.forEach { wsId ->
            val workstation = workstationMap[wsId] ?: return@forEach
            if (!workstationOriginalStatus.containsKey(wsId)) {
                workstationOriginalStatus[wsId] = workstation.status
            }
            if (workstation.status != WorkstationStatusEnum.UNAVAILABLE) {
                workstation.status = WorkstationStatusEnum.UNAVAILABLE
                log.debug("故障生效(工岗): id={}, tick={}", wsId, currentTick)
            }
        }

        // 退出故障区间的车辆：仅当该车不再被任何故障覆盖时恢复原状态
        val carIdsToRecover = carOriginalStatus.keys.filter { it !in activeCarIds }
        carIdsToRecover.forEach { carId ->
            val car = carMap[carId]
            val origin = carOriginalStatus.remove(carId)
            if (car != null && origin != null) {
                car.realTime.operationStatus = origin
                log.debug("故障结束(车): id={}, tick={}, 恢复为 {}", carId, currentTick, origin)
            }
        }

        // 退出故障区间的设备：仅当该设备不再被任何故障覆盖时恢复原状态
        val eqIdsToRecover = equipmentOriginalStatus.keys.filter { it !in activeEquipmentIds }
        eqIdsToRecover.forEach { eqId ->
            val eq = equipmentMap[eqId]
            val origin = equipmentOriginalStatus.remove(eqId)
            if (eq != null && origin != null) {
                eq.realTime.operationStatus = origin
                log.debug("故障结束(设备): id={}, tick={}, 恢复为 {}", eqId, currentTick, origin)
            }
        }

        // 退出故障区间的工岗：仅当该工岗不再被任何故障覆盖时恢复原状态
        val wsIdsToRecover = workstationOriginalStatus.keys.filter { it !in activeWorkstationIds }
        wsIdsToRecover.forEach { wsId ->
            val workstation = workstationMap[wsId]
            val origin = workstationOriginalStatus.remove(wsId)
            if (workstation != null && origin != null) {
                workstation.status = origin
                log.debug("故障结束(工岗): id={}, tick={}, 恢复为 {}", wsId, currentTick, origin)
            }
        }
    }

    fun stopSimulate() {
        log.info("^_^ 调用模拟器stopSimulate方法, 仿真完成, 同时终止模拟器、算法、PLC三个线程 ^_^")
        shouldStop = true
        plcSimulator?.stopPlcSimulate()
        when (config.simulateType) {
            SimulateTypeEnum.REAL_TIME -> {
                RedisUtil.StringOps.set("MISSION_MAX_TICK", currentTick.toString())
            }

            SimulateTypeEnum.OFF_LINE -> {
                // 更新仿真任务的部分基础信息
                updateTaskInfo(SimulateStatusEnum.FINISHED, 100F)
            }
        }
    }

    /**
     * 更新Postgresql中仿真任务的部分基础信息
     */
    private fun updateTaskInfo(simulateStatus: SimulateStatusEnum, simulateProgress: Float) {
        val task = taskManager.getTaskByMissionId(config.missionId)
        task.maxTick = currentTick
        task.simulateStatus = simulateStatus
        task.simulateProgress = simulateProgress
        taskManager.updateTask(task)
    }

    // tick 自增与落库解耦（保证 500ms 固定推进）
    private fun recordTick() {
        if (config.saveToDB) {
            influxRepository.saveSimulateTick(
                currentTick,
                config.missionId,
                cars,
                equipments,
                positionMap.values,
                workstationMap.values,
                pathMap.values,
                productMap.values
            )
        }
        currentTick++ // 每 500ms 固定 +1
    }

    /**
     * 如果存在，删除influxdb中已存在的仿真数据
     */
    private fun deleteDataIfExists() {
        val task = taskManager.getTaskByMissionId(config.missionId)
        /*        if (task.maxTick != null && task.maxTick!! > 0) {
                    influxRepository.deleteDataByMissionAndTick(config.missionId, task.maxTick)
                }*/
        influxRepository.deleteDataByMission(config.missionId)
    }

    /**
     * 获取车辆位置，如果车辆不存在，则返回空
     */
    fun getCarCoordinate(carId: Long): Coordinate? {
        return carMap[carId]?.realTime?.currentCoordinate
    }

    /**
     * 获取子车状态，若车辆不存在或不是子车，返回空列表
     */
    fun getSubCarStatus(carId: Long): List<SubCarStatusEnum> {
        val car = carMap[carId]
        if (car == null) {
            return listOf()
        } else {
            if (car !is SubCar) {
                return listOf()
            }
            return SubCarStatusEnum.fromStatus(
                car.realTime.loadStatus,
                car.realTime.topRodStatus,
                car.realTime.ferryStatus,
                car.realTime.operationStatus
            )
        }
    }

    /**
     * 获取摆渡车状态，若车辆不存在或不是摆渡车，返回空列表
     */
    fun getFerryStatus(carId: Long): List<FerryStatusEnum> {
        val car = carMap[carId]
        if (car == null) {
            return listOf()
        } else {
            if (car !is Ferry) {
                return listOf()
            }
            return FerryStatusEnum.fromStatus(
                car.realTime.operationStatus,
                car.realTime.subCarStatus
            )
        }
    }

    /**
     * 获取设备状态，若设备不存在，返回null
     */
    fun getEquipmentStatus(equipmentId: Long): EquipmentOperationStatusEnum? {
        val equipment = equipmentMap[equipmentId]
        if (equipment == null) {
            return null
        }
        return equipment.realTime.operationStatus
    }

    /**
     * 获取位置状态，若位置不存在，返回null
     */
    fun getPositionStatus(positionId: Long): PositionStatusEnum? {
        val position = positionMap[positionId]
        return position?.status
    }

    /**
     * 获取工位状态，若工位不存在，返回null
     */
    fun getWorkstationStatus(workstationId: Long): WorkstationStatusEnum? {
        val workstation = workstationMap[workstationId]
        return workstation?.status
    }

    /**
     * 获取路径状态，若路径不存在，返回null
     */
    fun getPathStatus(pathId: Long): PathStatusEnum? {
        return pathMap[pathId]?.status
    }

    /**
     * 获取车辆是否有当前事件
     */
    fun carHasCurrentEvent(carId: Long): Boolean {
        return carMap[carId]?.realTime?.currentEvent != null
    }


    /**
     * 获取车辆
     */
    fun getCar(carId: Long): Car {
        val carRealTime = carMap[carId] ?: throw BusinessException("未找到车辆")
        return carRealTime
    }

    /**
     * 获取设备
     */
    fun getEquipment(equipmentId: Long): Equipment {
        val equipmentRealTime = equipmentMap[equipmentId] ?: throw BusinessException("未找到设备")
        return equipmentRealTime
    }

    /**
     * 获取点位
     */
    fun getPosition(positionId: Long): PositionRealTime {
        val positionRealTime = positionMap[positionId] ?: throw BusinessException("未找到点位")
        return positionRealTime
    }

    /**
     * 获取工位
     */
    fun getWorkstation(workstationId: Long): WorkstationRealTime {
        val workstationRealTime = workstationMap[workstationId] ?: throw BusinessException("未找到工位")
        return workstationRealTime
    }

    /**
     * 事件完成回调方法
     */
    fun eventFinish(event: Event) {
        if (event.id != null) {
            deployThread?.eventFinish(event)
        } else {
            plcSimulator?.plcEventFinish(event)
        }
        log.debug("模拟器调用finish方法，事件：{} + {} + {}", event.id, event.eventName, event.instruction)
    }

    /**
     * 下发事件
     */
    fun issueEvent(event: Event) {
        log.debug("模拟器收到下发事件：{} + {} + {}", event.id, event.eventName, event.instruction)
        // 维护产品信息
        if (event.product != null) {
            productMap[event.product.id] = event.product
            // 如果有子产品，说明子产品已经合到当前产品中了，需要从productMap中暂时移除
            if (event.product.products != null) {
                event.product.products.forEach {
                    productMap.remove(it.id)
                }
            }
        }
        when (event.instruction.executeType) {
            ExecuteTypeEnum.FERRY, ExecuteTypeEnum.SUB_CAR -> {
                // 下发车辆事件
                issueCarEvent(event)
            }

            ExecuteTypeEnum.EQUIPMENT -> {
                // 下发设备事件
                issueEquipmentEvent(event)
            }

            ExecuteTypeEnum.WORK_STATION -> {
                // 下发工岗事件
                issueWorkstationEvent(event)
            }

            ExecuteTypeEnum.POSITION -> {
                // 下发点位事件
                issuePositionEvent(event)
            }

            else -> {}
        }
    }

    /**
     * 下发车辆事件
     */
    private fun issueCarEvent(event: Event) {
        val car = carMap[event.instruction.id]
        if (car == null) {
            // todo 没找到车，返回错误
            return
        }
        // todo 判断车辆状态是否允许执行该命令
        car.realTime.currentEvent = event
//        log.debug("【事件 ${event.id}】 车辆 ${car.id} 执行 ${event.instruction.instructions} 指令，目标坐标: ${event.instruction.runAimCoordinate}")
        when (event.instruction.instructions) {
            InstructionsEnum.RUNNING -> {
                // =========== 测试用
                val runAimCoordinate = event.instruction.runAimCoordinate
                val targetX = runAimCoordinate.x
                val targetY = runAimCoordinate.y

                val currentX = car.realTime.currentCoordinate.x
                val currentY = car.realTime.currentCoordinate.y
                if (abs(targetX - currentX) >= 0.3 && abs(targetY - currentY) >= 0.3) {
                    println("【】【】【】：" + event)
                    throw Exception("目标位置不合法")
                }
                // 测试用  ============

                car.move(event.instruction.runAimCoordinate)
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.carMove(car, event)
                    SimulateTypeEnum.OFF_LINE -> {}
                }
            }

            InstructionsEnum.RISING -> {
//                this.deployThread?.let { (car as SubCar).topRodRaise(timer, it) }
                // 车辆下发顶杆升指令
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.carRising(car, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.CarFunctions().risingInstruction(car, event)
                }
            }

            InstructionsEnum.LANDING -> {
//                this.deployThread?.let { (car as SubCar).topRodDown(timer, it) }
                // 车辆下发顶杆降指令
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.carLanding(car, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.CarFunctions().landingInstruction(car, event)
                }
            }

            InstructionsEnum.CAR_WAITING -> {
                // 车辆下发等待指令
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.carWaiting(car, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.CarFunctions().waitInstruction(car, event)
                }
            }

            else -> {}
        }
    }

    /**
     * 下发设备事件
     */
    private fun issueEquipmentEvent(event: Event) {
        val equipment = equipmentMap[event.instruction.id]
        if (equipment == null) {
            // todo 没找到设备，返回错误
            return
        }
        // todo 判断设备状态是否允许执行该命令
        equipment.realTime.currentEvent = event
        when (event.instruction.instructions) {
            InstructionsEnum.OPEN -> {
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.equipmentOpen(equipment, event)
                    SimulateTypeEnum.OFF_LINE -> this.deployThread?.let { equipment.open(timer, it) }
                }
            }

            InstructionsEnum.OFF -> {
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.equipmentOff(equipment, event)
                    SimulateTypeEnum.OFF_LINE -> this.deployThread?.let { equipment.off(timer, it) }
                }
            }

            InstructionsEnum.NOTICE -> {
                //                        this.deployThread?.let { equipment.notice(timer, event.instruction, it) }
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.equipmentNotice(equipment, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.EquipmentFunctions()
                        .equipmentNotice(equipment, event)
                }
            }

            InstructionsEnum.USE_MOLTEN_IRON -> {
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.equipmentUseMoltenIron(equipment, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.EquipmentFunctions()
                        .useMoltenIron(equipment, event)
                }
            }

            InstructionsEnum.PREPARE_MOLTEN_IRON -> {
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.equipmentPrepareMoltenIron(equipment, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.EquipmentFunctions()
                        .prepareMoltenIron(equipment, event)
                }
            }

            else -> {}
        }
    }

    /**
     * 下发工岗事件
     */
    private fun issueWorkstationEvent(event: Event) {
        val workstation = workstationMap[event.instruction.id]
        if (workstation == null) {
            throw BusinessException("未找到工位")
        }
        // 【模拟工厂控制部分】
        plcSimulator?.triggerByWorkstation(workstation)
        when (event.instruction.instructions) {
            // 通知指令。状态改为待搬运(需要隔一段时间)
            InstructionsEnum.NOTICE -> {
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.workstationNotice(workstation, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.WorkstationFunctions()
                        .workstationNotice(workstation, event)
                }
            }

            else -> {}
        }
    }

    /**
     * 下发点位事件
     */
    private fun issuePositionEvent(event: Event) {
        val position = positionMap[event.instruction.id]
        if (position == null) {
            throw BusinessException("未找到点位")
        }
        when (event.instruction.instructions) {
            InstructionsEnum.NOTICE -> {
                when (config.simulateType) {
                    SimulateTypeEnum.REAL_TIME -> this.plcClient.positionNotice(position, event)
                    SimulateTypeEnum.OFF_LINE -> this.simulatorFunctions.PositionFunctions()
                        .positionNotice(position, event)
                }
            }

            else -> {}
        }
    }

}
