package cn.hex.ddp.manufacture.api.simulator.ws.listener

import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.socketio.SocketInstance
import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.listener.DataListener

@Slf4j
class AppointTaskListener : DataListener<String> {
    override fun onData(
        client: SocketIOClient,
        data: String,
        ackSender: AckRequest
    ) {
        log.info("AppointTaskListener, TaskId:{}", data)
        SocketInstance.clientAppointTask[client.sessionId.toString()] = data.toLong()
    }
}