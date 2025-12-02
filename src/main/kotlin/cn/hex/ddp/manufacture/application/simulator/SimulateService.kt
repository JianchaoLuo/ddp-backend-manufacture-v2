package cn.hex.ddp.manufacture.application.simulator


import cn.hex.ddp.manufacture.api.simulator.rest.vo.SimulateStatusVO
import cn.hex.ddp.manufacture.application.car.service.CarService
import cn.hex.ddp.manufacture.application.configuration.service.ConfigurationService
import cn.hex.ddp.manufacture.application.equipment.service.EquipmentService
import cn.hex.ddp.manufacture.application.order.service.OrderService
import cn.hex.ddp.manufacture.application.path.service.PathService
import cn.hex.ddp.manufacture.application.simulator.converter.SimulateDataConverter
import cn.hex.ddp.manufacture.application.simulator.processor.CommandProcessor
import cn.hex.ddp.manufacture.application.task.dto.out.TaskDTO
import cn.hex.ddp.manufacture.application.workstation.service.WorkstationService
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager
import cn.hex.ddp.manufacture.domain.simulator.enums.SimulateStatusEnum
import cn.hex.ddp.manufacture.domain.simulator.enums.SimulateTypeEnum
import cn.hex.ddp.manufacture.domain.simulator.model.MissionConfig
import cn.hex.ddp.manufacture.domain.simulator.model.cache.SimulateCache
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.SubCar
import cn.hex.ddp.manufacture.domain.task.model.Fault
import cn.hex.ddp.manufacture.infrastructure.algorithm.AlgorithmEndpoint
import cn.hex.ddp.manufacture.infrastructure.algorithm.DeployThread
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput
import cn.hex.ddp.manufacture.infrastructure.common.converter.AlgorithmDataConverter
import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.plcmodule.simulator.PLCSimulator
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Slf4j
@Service
open class SimulateService {
    lateinit var simulateThread: CommandProcessor
    lateinit var deployThread: DeployThread

    @Autowired
    lateinit var carService: CarService

    @Autowired
    lateinit var equipmentService: EquipmentService

    @Autowired
    lateinit var pathService: PathService

    @Autowired
    lateinit var workstationService: WorkstationService

    @Autowired
    lateinit var configurationService: ConfigurationService

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var orderManager: OrderManager

    // 用于JSON打印
    @Autowired
    lateinit var objectMapper: ObjectMapper


    @PostConstruct
    fun init() {
        log.debug("SimulateService初始化")
    }

    @Async
    fun runSimulate(taskDTO: TaskDTO) {
        //打印 taskDTO 全量内容（优先 JSON，失败回退到 toString）
        try {
            val json = objectMapper.writeValueAsString(taskDTO)
            log.info("runSimulate 入参 TaskDTO: {}", json)
        } catch (e: Exception) {
            log.warn("TaskDTO 序列化失败，使用 toString，原因: {}", e.message, e)
            log.info("runSimulate 入参 TaskDTO: {}", taskDTO)
        }
        SimulateCache.reset()
        // 开始，设置仿真计划开始时间
        SimulateCache.T1 = taskDTO.plannedProductionDate
        // 准备仿真上下文
        val prepareSimulateContext = prepareSimulateContext(taskDTO)
        // 记录准备仿真上下文的完成时间
        SimulateCache.T2 = LocalDateTime.now()
        // 算法计算仿真结果，这个结果是个指令集合
        val doSimulate = AlgorithmEndpoint.preDoSimulate(prepareSimulateContext)
        SimulateCache.T3 = LocalDateTime.now()

        // 将此次算法运行结果中的订单时间转换为甘特图，并存入Redis中
        orderManager.addOrderGanttChart(taskDTO.missionId, taskDTO.plannedProductionDate, doSimulate.orderTimes)
        // 启动模拟器线程，传入故障信息
        simulateThread = startSimulateThread(taskDTO.plannedProductionDate, taskDTO.missionId, taskDTO.faults)
        // 启动算法线程
        deployThread = AlgorithmEndpoint.startDeployThread(doSimulate)
        // 双向绑定
        simulateThread.setDeployThread(deployThread)
        deployThread.setSimulateThread(simulateThread)
        // 将模拟器跟PLC也绑定
        val plcSimulator = PLCSimulator()
        plcSimulator.setCommandProcessor(simulateThread)
        simulateThread.setPLCSimulator(plcSimulator)

        SimulateCache.T4 = LocalDateTime.now()
        SimulateCache.deltaT = Duration.between(SimulateCache.T1, SimulateCache.T4).toMillis()
        // 正式开始
        simulateThread.go()
        deployThread.go()
        plcSimulator.startScheduling()
    }

    fun prepareSimulateContext(taskDTO: TaskDTO): SimulateInput {
        val input = SimulateInput()
        val allocationInputs = ArrayList<AllocationInput>()
        // 转换子车和摆渡车
        val allCar = carService.allCar
        val subCars = AlgorithmDataConverter.carDetailDTOsToSubCars(allCar)
        val subCarsMap = subCars.groupBy { it.nameEnum.area }
        val ferries = AlgorithmDataConverter.carDetailDTOsToFerries(allCar)
        val ferriesMap = ferries.groupBy { it.nameEnum.area }
        // 转换设备列表
        val allEquipments = equipmentService.allEquipment
        val equipments = AlgorithmDataConverter.equipmentVOToEquipments(allEquipments)
        val equipmentsMap = equipments.groupBy { it.nameEnum.area }
        // 转换点位列表
        val allPosition = configurationService.allPosition
        val positions = AlgorithmDataConverter.positionDTOsToPositions(allPosition)
        val positionsMap = positions.groupBy { it.nameEnum.area }
        // 转换工岗列表
        val allWorkstation = workstationService.allWorkstation
        val workStations = AlgorithmDataConverter.workstationVOsToWorkStations(allWorkstation)
        val workStationsMap = workStations.groupBy { it.nameEnum.area }
        // 转换路径列表
        val allPath = pathService.allPath
        val paths = AlgorithmDataConverter.pathDTOsToPaths(allPath)
        val pathsMap = paths.groupBy { it.nameEnum.area }

        val maps = listOf(subCarsMap, ferriesMap, equipmentsMap, positionsMap, workStationsMap, pathsMap)
        for (area in AreaEnum.entries) {
            // 如果当前区域所有数据都为空，则跳过
            if (maps.none { it.containsKey(area) }) {
                continue
            }
            val allocationInput = AllocationInput(
                subCarsMap.getOrDefault(area, emptyList()),
                ferriesMap.getOrDefault(area, emptyList()),
                equipmentsMap.getOrDefault(area, emptyList()),
                positionsMap.getOrDefault(area, emptyList()),
                workStationsMap.getOrDefault(area, emptyList()),
                pathsMap.getOrDefault(area, emptyList()),
                area
            )
            allocationInputs.add(allocationInput)
        }

        val orderInputs = ArrayList<OrderInput>()
        // 转换订单和产品等信息
        val taskItemsMap = taskDTO.taskItems.groupBy { it.order }
        for (entry in taskItemsMap.entries) {
            val order = entry.key
            val taskItems = entry.value
            val orderInput = AlgorithmDataConverter.toOrderInput(order, taskItems)
            orderInputs.add(orderInput)
        }

        // TODO：待结合实际情况修改或优化
        val analogInput = AnalogInput(
            subCars.find { it.id == 1806898891212197890 },
            subCars.find { it.id == 1817456665160683522 },
            subCars.find { it.id == 1817456988612825089 },
            subCars.find { it.id == 1817886619438518273 },
            subCars.find { it.id == 1806899642168774658 },
            subCars.find { it.id == 1806898464978636801 },
            subCars.find { it.id == 1813500565670334466 },
            equipments.find { it.id == 1796862790497665025 },
            equipments.find { it.id == 1796862790678020098 },
            equipments.find { it.id == 1796862790610911234 },
            equipments.find { it.id == 1796862790812237825 },
            equipments.find { it.id == 1796862790547996675 },
            equipments.find { it.id == 1796862790745128961 },
        )

        input.orderInputs = orderInputs
        input.allocationInputs = allocationInputs
        input.analogInput = analogInput
        return input
    }

    fun getSimulateStatus(): SimulateStatusVO {
        return SimulateStatusVO(SimulateStatusEnum.FINISHED)
    }

    fun startSimulateThread(
        expectStartTime: LocalDateTime,
        newMissionId: Long,
        faults: List<Fault>?
    ): CommandProcessor {
        val simulateThread = CommandProcessor(
            config = MissionConfig(
                missionId = newMissionId,
                startTime = expectStartTime,
                cars = carService.allCar.filter { it.type == CarTypeEnum.FERRY_CAR || it.type == CarTypeEnum.SUB_CAR }
                    .map {
                        when (it.type) {
                            CarTypeEnum.FERRY_CAR -> SimulateDataConverter.carDetailDTOToFerry(it)
                            CarTypeEnum.SUB_CAR -> SimulateDataConverter.carDetailDTOToSubCar(it)
                            else -> SubCar(id = -1L)
                        }
                    },
                equipments = equipmentService.allEquipment.map {
                    SimulateDataConverter.equipmentVOToEquipment(it)
                },
                paths = pathService.allPath.map {
                    SimulateDataConverter.pathDTOToPath(it)
                },
                positions = configurationService.allPosition.map {
                    SimulateDataConverter.positionDTOToPosition(it)
                },
                workstations = workstationService.allWorkstation.map {
                    SimulateDataConverter.workstationVOToWorkstation(it)
                },
                simulateType = SimulateTypeEnum.OFF_LINE,
                faults = faults // 传入故障列表
            )
        )
        simulateThread.start()
        return simulateThread
    }

    private fun getNewMissionId(): Long {
        return RedisUtil.StringOps.incrBy("MISSION_ID", 1)
    }
}
