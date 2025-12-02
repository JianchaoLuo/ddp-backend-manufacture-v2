package cn.hex.ddp.manufacture.infrastructure.socketio

import cn.hex.ddp.manufacture.application.simulator.RealTimePusher
import com.corundumstudio.socketio.SocketIOClient
import java.time.LocalDateTime

object SocketInstance {
    var socketClients = mutableMapOf<String, SocketIOClient>()
    val clientRealTimePusherThread = mutableMapOf<String, Thread>()
    val clientInitTime = mutableMapOf<String, LocalDateTime>()
    val clientReadyShow = mutableMapOf<String, Boolean>()
    val clientAppointTask = mutableMapOf<String, Long>()
    fun insertSocketClient(key: String, value: SocketIOClient) {
        socketClients[key] = value
    }

    fun removeSocketClient(key: String) {
        val client = getSocketClient(key)
        client?.disconnect()
        socketClients.remove(key)
    }

    fun getSocketClient(key: String): SocketIOClient? {
        return socketClients[key]
    }

    fun clearThisClient(key: String) {
        // 这里如果直接clientRealTimePusherThread[key]?.interrupt()，无法完全终止线程
        val realTimePusherThread = clientRealTimePusherThread[key]
        if (realTimePusherThread is RealTimePusher) {
            realTimePusherThread.terminal()
        }
        clientRealTimePusherThread.remove(key)
        clientInitTime.remove(key)
        clientReadyShow.remove(key)
        clientAppointTask.remove(key)
        removeSocketClient(key)

    }
}
