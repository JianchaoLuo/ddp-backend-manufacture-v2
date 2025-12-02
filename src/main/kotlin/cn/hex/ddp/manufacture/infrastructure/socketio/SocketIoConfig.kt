package cn.hex.ddp.manufacture.infrastructure.socketio

import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner
import org.springframework.context.annotation.Bean

@Slf4j
@org.springframework.context.annotation.Configuration
class SocketIoConfig {

    companion object {
        const val WEB_SOCKET_PORT = 8090
        const val ORIGIN = "*"
    }

    @Bean
    fun socketIOServer(): SocketIOServer {
        val config: Configuration = Configuration()
        config.getSocketConfig().setReuseAddress(true)
        config.setPort(WEB_SOCKET_PORT)

        config.origin = ORIGIN
        config.jsonSupport = SocketIoJacksonSupport()
        config.setAuthorizationListener(SocketIoAuthHandler());
        config.setExceptionListener(
            SocketIoExceptionListener()
        )
        return SocketIOServer(config)
    }

    /**
     * 用于扫描 netty-socketio 注解 比如 @OnConnect、@OnEvent
     */
    @Bean
    fun springAnnotationScanner(): SpringAnnotationScanner {
        return SpringAnnotationScanner(socketIOServer());
    }

}
