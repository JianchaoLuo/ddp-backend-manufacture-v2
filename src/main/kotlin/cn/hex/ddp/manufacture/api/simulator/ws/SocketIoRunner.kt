package cn.hex.ddp.manufacture.api.simulator.ws

import cn.hex.ddp.manufacture.api.simulator.ws.listener.AppointTaskListener
import cn.hex.ddp.manufacture.api.simulator.ws.listener.ReadyShowListener
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.socketio.InEvents
import com.corundumstudio.socketio.SocketIOServer
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.jvm.java

@Slf4j
@Component
class SocketIoRunner : CommandLineRunner, DisposableBean {
    lateinit var socketIOServer: SocketIOServer

    @Autowired
    constructor(socketIOServer: SocketIOServer) {
        this.socketIOServer = socketIOServer
    }

    override fun run(vararg args: String?) {
//        socketIOServer.addEventListener(InEvents.INIT_TIME, InitTimeEvent::class.java, InitTimeListener())
        socketIOServer.addEventListener(InEvents.READY_SHOW, Void::class.java, ReadyShowListener())
        socketIOServer.addEventListener(InEvents.APPOINT_TASK, String::class.java, AppointTaskListener())
        socketIOServer.start()
        log.info("socket io server started successfully.")
    }

    override fun destroy() {
        socketIOServer.stop()
    }
}
