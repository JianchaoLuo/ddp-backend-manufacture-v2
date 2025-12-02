package cn.hex.ddp.manufacture.domain.simulator.model.simulation

import cn.dev33.satoken.SaManager.config
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.DeployThread
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Instruction
import java.util.*
import kotlin.random.Random

data class EquipmentConfig(
    val processTime: Double = 5.0,
    // 电炉烧制时长
    val electricFurnaceFiringTime: Double = 30.0
)

data class EquipmentMeta(
    val config: EquipmentConfig = EquipmentConfig(),
    val nameEnum: EquipmentNameEnum = EquipmentNameEnum.DEFAULT,
    val name: String = "",
)

data class EquipmentRealTime(
    var operationStatus: EquipmentOperationStatusEnum = EquipmentOperationStatusEnum.STANDBY,
    var currentAction: String = "",
    var currentEvent: Event? = null,
    var moltenIronQuantity: Double = 0.0,
)

data class Equipment(
    val id: Long,
    val meta: EquipmentMeta = EquipmentMeta(),
    val realTime: EquipmentRealTime = EquipmentRealTime(),
) {
    fun eventFinish() {
        realTime.currentEvent = null
    }

    fun open(timer: Timer, deployThread: DeployThread) {
        this.realTime.operationStatus = EquipmentOperationStatusEnum.STANDBY
        deployThread.eventFinish(this.realTime.currentEvent)
        this.eventFinish()
    }

    fun off(timer: Timer, deployThread: DeployThread) {
        this.realTime.operationStatus = EquipmentOperationStatusEnum.OFFLINE
        deployThread.eventFinish(this.realTime.currentEvent)
        this.eventFinish()
    }

    fun notice(
        timer: Timer,
        instruction: Instruction,
        deployThread: DeployThread
    ) {
        val noise = Random.nextDouble(-15.0, 15.0)
        this.realTime.operationStatus = EquipmentOperationStatusEnum.WORKING
        val equipment = this
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    equipment.realTime.operationStatus = EquipmentOperationStatusEnum.FINISHED
                    deployThread.eventFinish(equipment.realTime.currentEvent)
                    equipment.eventFinish()
                }
            },
            ((meta.config.processTime + noise) * 1000).toLong()
        )
    }


    override fun toString(): String {
        return "$id ${realTime.operationStatus} $config ${realTime.currentEvent}"
    }


}
