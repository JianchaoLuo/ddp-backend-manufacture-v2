package cn.hex.ddp.manufacture.application.simulator

import cn.hex.ddp.manufacture.domain.common.exception.BusinessException
import cn.hex.ddp.manufacture.domain.simulator.model.tick.*
import cn.hex.ddp.manufacture.domain.task.manager.TaskManager
import cn.hex.ddp.manufacture.infrastructure.influx.po.*
import cn.hex.ddp.manufacture.infrastructure.influx.repository.InfluxRepository
import cn.hex.ddp.manufacture.infrastructure.socketio.OutEvents
import cn.hex.ddp.manufacture.infrastructure.socketio.SocketInstance
import cn.hutool.extra.spring.SpringUtil
import java.time.Duration
import java.time.LocalDateTime
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.math.min


class RealTimePusher(
    private val sessionId: String,

    ) : Thread() {
    private var isRunning = true
    private var initTime: LocalDateTime? = null
    private var startRealTime = LocalDateTime.now()
    private var timeInterval: Duration? = null
    private var client = SocketInstance.socketClients[sessionId]!!

    private var mission: Long = 1L
    private var maxTick: Long = 1000000L

    private var tick = 0L

    private val fetchBatch = 200L

    private var equipmentTickData = mapOf<Long, List<EquipmentTickData>>();
    private var workstationTickData = mapOf<Long, List<WorkstationTickData>>();
    private var positionTickData = mapOf<Long, List<PositionTickData>>();
    private var carTickData = mapOf<Long, List<CarTickData>>()
    private var productTickData = mapOf<Long, List<ProductTickData>>()

    private val timers = mutableListOf<Timer>()

    private var influxRepository: InfluxRepository? = null
    private var taskManager: TaskManager = SpringUtil.getBean(TaskManager::class.java)


    override fun run() {
        init()
        updateTickData(fetchBatch)

        // 开启几个timer分别推送数据
        runTimers()
        while (isRunning) {
            sleep(500)
            tick++
            // 如果tick大于maxTick，说明已经到达最后一批数据，停止线程
            if (tick > maxTick) {
                isRunning = false
            }
        }
        terminalTimers()
        client.sendEvent(OutEvents.END_SHOW, "")
    }

    private fun init() {
/*        val missionId: String? = RedisUtil.StringOps.get("MISSION_ID")
        if (missionId == null) {
            throw BusinessException("没找到MISSION_ID")
        } else {
            mission = missionId.toLong()
        }
        val missionMaxTick: String? = RedisUtil.StringOps.get("MISSION_MAX_TICK")
        if (missionMaxTick == null) {
            throw BusinessException("没找到MISSION_MAX_TICK")
        } else {
            maxTick = missionMaxTick.toLong()
        }*/
        val taskId = SocketInstance.clientAppointTask[sessionId]
        if (taskId == null) {
            SocketInstance.clientReadyShow.remove(client.sessionId.toString())
            throw BusinessException("没有指定仿真任务ID")
        }
        val task = taskManager.getTaskById(taskId)
        if (task == null) {
            SocketInstance.clientReadyShow.remove(client.sessionId.toString())
            throw BusinessException("没找到任务, taskId: $taskId")
        }
        if (task.missionId == null) {
            SocketInstance.clientReadyShow.remove(client.sessionId.toString())
            throw BusinessException("没找到MISSION_ID")
        } else {
            mission = task.missionId
        }
        if (task.maxTick == null) {
            SocketInstance.clientReadyShow.remove(client.sessionId.toString())
            throw BusinessException("没找到MISSION_MAX_TICK")
        } else {
            maxTick = task.maxTick
        }
//        print("missionId: $mission, maxTick: $maxTick")
        this.initTime = SocketInstance.clientInitTime[sessionId]
        this.timeInterval = Duration.between(initTime, startRealTime)
        this.influxRepository = SpringUtil.getBean<InfluxRepository>(InfluxRepository::class.java)
    }

    private fun runTimers() {
        timers.add(
            fixedRateTimer(
                OutEvents.CAR_REAL_TIME,
                true,
                0,
                500
            ) {
                client.sendEvent(
                    OutEvents.CAR_REAL_TIME,
                    carTickData[tick]
                )
            }
        )
        timers.add(
            fixedRateTimer(
                OutEvents.EQUIPMENT_REAL_TIME,
                true,
                0,
                500
            ) {
                client.sendEvent(
                    OutEvents.EQUIPMENT_REAL_TIME,
                    equipmentTickData[tick]
                )
            }
        )
        timers.add(
            fixedRateTimer(
                OutEvents.WORKSTATION_REAL_TIME,
                true,
                0,
                500
            ) {
                client.sendEvent(
                    OutEvents.WORKSTATION_REAL_TIME,
                    workstationTickData[tick]
                )
            }
        )
        timers.add(
            fixedRateTimer(
                OutEvents.POSITION_REAL_TIME,
                true,
                0,
                500
            ) {
                client.sendEvent(
                    OutEvents.POSITION_REAL_TIME,
                    positionTickData[tick]
                )
            }
        )
        timers.add(
            fixedRateTimer(
                OutEvents.PRODUCT_REAL_TIME,
                true,
                0,
                1000 * 5
            ) {
                client.sendEvent(
                    OutEvents.PRODUCT_REAL_TIME,
                    productTickData[tick]
                )
            }
        )
        timers.add(
            fixedRateTimer(
                OutEvents.CHART_REAL_TIME,
                true,
                0,
                1000 * 5
            ) {
                client.sendEvent(
                    OutEvents.CHART_REAL_TIME,
                    mapOf(
                        "carChartData" to carTickData[tick]?.map { it.toCarChartData() },
                        "equipmentChartData" to equipmentTickData[tick],
                        "workstationChartData" to workstationTickData[tick]
                    )
                )
            }
        )
        timers.add(
            fixedRateTimer(
                "REFRESH_TICK_DATA",
                true,
                1000,
                (fetchBatch * 500 * 0.6).toLong()
            ) {
                // 更新数据
                val rightTick = min(maxTick, tick + fetchBatch)



                updateTickData(rightTick)
            }
        )
    }

    private fun updateTickData(rightTick: Long) {
        equipmentTickData = influxRepository!!.getTickData(
            mission,
            LongRange(tick, rightTick),
            EquipmentRealTimePO::class.java,
            EquipmentRealTimePO::toEquipmentTickData
        )
        workstationTickData = influxRepository!!.getTickData(
            mission,
            LongRange(tick, rightTick),
            WorkstationRealTimePO::class.java,
            WorkstationRealTimePO::toWorkstationTickData
        )
        carTickData = influxRepository!!.getTickData(
            mission,
            LongRange(tick, rightTick),
            CarRealTimePO::class.java,
            CarRealTimePO::toCarTickData
        )
        positionTickData = influxRepository!!.getTickData(
            mission,
            LongRange(tick, rightTick),
            PositionRealTimePO::class.java,
            PositionRealTimePO::toPositionTickData
        )
        productTickData = influxRepository!!.getTickData(
            mission,
            LongRange(tick, rightTick),
            ProductRealTimePO::class.java,
            ProductRealTimePO::toProductTickData
        )
    }

    private fun terminalTimers() {
        timers.forEach {
            it.cancel()
        }
    }

    fun terminal() {
        isRunning = false
        // 如果线程停止，需要及时停止所有的timer定时器
        terminalTimers()
    }
}
