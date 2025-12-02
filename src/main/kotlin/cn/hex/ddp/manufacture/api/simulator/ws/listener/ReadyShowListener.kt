package cn.hex.ddp.manufacture.api.simulator.ws.listener

import cn.hex.ddp.manufacture.application.simulator.RealTimePusher
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.socketio.SocketInstance
import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.listener.DataListener

@Slf4j
class ReadyShowListener : DataListener<Void> {
    override fun onData(
        client: SocketIOClient,
        data: Void?,
        ackSender: AckRequest
    ) {
        log.info("ReadyShowListener")
        if (SocketInstance.clientReadyShow.containsKey(client.sessionId.toString())) {
            log.info("client already ready show.")
            return
        }
        SocketInstance.clientReadyShow.put(client.sessionId.toString(), true)
        val pusherThread = RealTimePusher(client.sessionId.toString())
        SocketInstance.clientRealTimePusherThread.put(
            client.sessionId.toString(),
            pusherThread
        )
        pusherThread.start()
    }
}
