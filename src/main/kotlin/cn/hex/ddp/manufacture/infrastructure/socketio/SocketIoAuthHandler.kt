package cn.hex.ddp.manufacture.infrastructure.socketio

import cn.hex.ddp.manufacture.domain.simulator.model.cache.SimulateCache
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import com.corundumstudio.socketio.AuthorizationListener
import com.corundumstudio.socketio.AuthorizationResult
import com.corundumstudio.socketio.HandshakeData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Slf4j
class SocketIoAuthHandler : AuthorizationListener {
    override fun getAuthorizationResult(data: HandshakeData): AuthorizationResult? {
        var initTimeArr = data.urlParams["initTime"]
        if (initTimeArr == null) {
            return AuthorizationResult(true, mapOf("initTime" to SimulateCache.T1))
        }
        var initTime: LocalDateTime? = null
        try {
            initTime = LocalDateTime.parse(initTimeArr[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            log.info("initTime:{}", initTime)
        } catch (_: Exception) {
            return AuthorizationResult(true, mapOf("initTime" to SimulateCache.T1))
        }
        log.info("authorization:{}", data)
        return AuthorizationResult(true, mapOf("initTime" to initTime))
    }
}
