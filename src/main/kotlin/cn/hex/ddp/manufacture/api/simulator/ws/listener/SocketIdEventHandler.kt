package cn.hex.ddp.manufacture.api.simulator.ws.listener

import cn.hex.ddp.manufacture.application.car.service.CarService
import cn.hex.ddp.manufacture.application.configuration.service.ConfigurationService
import cn.hex.ddp.manufacture.application.equipment.service.EquipmentService
import cn.hex.ddp.manufacture.application.path.service.PathService
import cn.hex.ddp.manufacture.application.workstation.service.WorkstationService
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.socketio.OutEvents
import cn.hex.ddp.manufacture.infrastructure.socketio.SocketInstance
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.OnConnect
import com.corundumstudio.socketio.annotation.OnDisconnect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.collections.forEach

@Slf4j
@Component
class SocketIdEventHandler {
    /**
     * 服务器socket对象
     */
    lateinit var socketIoServer: SocketIOServer;
    lateinit var configurationService: ConfigurationService
    lateinit var workstationService: WorkstationService
    lateinit var pathService: PathService
    lateinit var equipmentService: EquipmentService
    lateinit var carService: CarService

    @Autowired
    constructor(
        socketIoServer: SocketIOServer,
        configurationService: ConfigurationService,
        workstationService: WorkstationService,
        pathService: PathService,
        equipmentService: EquipmentService,
        carService: CarService
    ) {
        this.socketIoServer = socketIoServer
        this.configurationService = configurationService
        this.workstationService = workstationService
        this.pathService = pathService
        this.equipmentService = equipmentService
        this.carService = carService
    }

    @OnConnect
    fun onConnect(client: SocketIOClient) {
        var initTime = client.get<LocalDateTime>("initTime")
        if (initTime != null) {
            SocketInstance.clientInitTime.put(client.sessionId.toString(), initTime)
        }
        log.info("客户端{}连接成功", client.getSessionId());
        SocketInstance.insertSocketClient(client.sessionId.toString(), client)
        pushPosition(client)
        pushPath(client)
        pushWorkstation(client)
        pushEquipment(client)
        pushCar(client)
    }

    @OnDisconnect
    fun onDisconnect(client: SocketIOClient) {
        log.info("客户端{}断开连接", client.getSessionId());
        val sessionId = client.sessionId.toString()
        SocketInstance.clearThisClient(sessionId);
    }

    companion object {
        fun broadcast(event: String, message: Any) {
            SocketInstance.socketClients.forEach {
                if (it.value.isChannelOpen) {
                    it.value.sendEvent(event, message)
                }
            }
        }
    }


    fun pushPosition(client: SocketIOClient) {
        client.sendEvent(OutEvents.META_POSITION, configurationService.getAllPosition())
    }

    fun pushPath(client: SocketIOClient) {
        client.sendEvent(OutEvents.META_PATH, pathService.getAllPath())
    }

    fun pushWorkstation(client: SocketIOClient) {
        client.sendEvent(OutEvents.META_WORKSTATION, workstationService.getAllWorkstation())
    }

    fun pushEquipment(client: SocketIOClient) {
        client.sendEvent(OutEvents.META_EQUIPMENT, equipmentService.getAllEquipment())
    }

    fun pushCar(client: SocketIOClient) {
        // todo 添加车头方向 向上，向下，向左，向右
        client.sendEvent(OutEvents.META_CAR, carService.getAllCar())
    }
}
