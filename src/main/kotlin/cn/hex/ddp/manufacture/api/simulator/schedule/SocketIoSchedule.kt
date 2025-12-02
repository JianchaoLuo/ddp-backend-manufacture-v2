package cn.hex.ddp.manufacture.api.simulator.schedule

import cn.hex.ddp.manufacture.api.simulator.ws.listener.SocketIdEventHandler
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.Equipment
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.socketio.SocketIoConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Slf4j
@Component
class SocketIoSchedule {
    @Autowired
    lateinit var socketIoConfig: SocketIoConfig

    /**
     * 每0.01秒钟推送一次
     */
//    @Scheduled(fixedRate = 1000)
    fun pushRealTimeData() {
//        log.warn("当前有{}个客户端连接", socketIoEntry.server.allClients.size)
//        SimulationVO build =
//        SimulationVO date = simulationService . getOnDisplayData ().poll();
////        log.info("推送的数据为{}", date);
////        socketIoConfig.broadCast(WsResponseActionEnum.SIMULATION1.name(), build);
//        if (count++ % 10 == 0) {
//            socketIoConfig.broadCast(WsResponseActionEnum.SIMULATION2.name(), date);
//            count = count % 10;
//        }
        SocketIdEventHandler.broadcast(
            "PING",
            Equipment(123123)
        )
    }
}
