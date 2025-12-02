package cn.hex.ddp.manufacture.infrastructure.socketio

import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.socketio.enums.SocketIoResponseActionEnum
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.listener.ExceptionListener
import io.netty.channel.ChannelHandlerContext
import java.lang.Exception

@Slf4j
class SocketIoExceptionListener : ExceptionListener {
    override fun onEventException(
        e: Exception?,
        args: List<Any?>?,
        client: SocketIOClient?
    ) {
        log.error(e?.message, e)
        client?.sendEvent(SocketIoResponseActionEnum.ERROR.name, ErrorVO(ErrorVO.INTERNAL_ERROR))
    }

    override fun onDisconnectException(e: Exception?, client: SocketIOClient?) {
        log.error(e?.message, e)
        client?.sendEvent(SocketIoResponseActionEnum.ERROR.name, ErrorVO(ErrorVO.CLIENT_DISCONNECT))
    }

    override fun onConnectException(e: Exception?, client: SocketIOClient?) {
        log.error(e?.message, e)
        client?.sendEvent(SocketIoResponseActionEnum.ERROR.name, ErrorVO(ErrorVO.CONNECT_ERROR))
    }

    @Deprecated("This method is not used")
    override fun onPingException(e: Exception?, client: SocketIOClient?) {
        log.error(e?.message, e)
        client?.sendEvent(SocketIoResponseActionEnum.ERROR.name, ErrorVO(ErrorVO.PING_ERROR))
    }

    override fun onPongException(e: Exception?, client: SocketIOClient?) {
        log.error(e?.message, e)
        client?.sendEvent(SocketIoResponseActionEnum.ERROR.name, ErrorVO(ErrorVO.PONG_ERROR))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, e: Throwable?): Boolean {
        log.error(e?.message, e);
        ctx?.channel()?.writeAndFlush(
            listOf<Any>(
                SocketIoResponseActionEnum.ERROR.name,
                ErrorVO(ErrorVO.INTERNAL_ERROR)
            )
        );
        return false;
    }

    override fun onAuthException(e: Throwable?, client: SocketIOClient?) {
        log.error(e?.message, e)
        client?.sendEvent(SocketIoResponseActionEnum.ERROR.name, ErrorVO(ErrorVO.AUTH_FAILED))
    }
}

data class ErrorVO(val msg: String) {

    companion object {
        const val INTERNAL_ERROR = "服务器内部错误，请稍后重试";
        const val TOKEN_PARSE_ERROR = "token解析错误";
        const val JSON_PROCESS_ERROR = "数据格式异常";
        const val UNRECOGNIZED_ACTION = "无法识别的行为";
        const val AUTH_FAILED = "认证错误";
        const val CLIENT_DISCONNECT = "连接断开";
        const val CONNECT_ERROR = "连接出现问题";
        const val PING_ERROR = "PING出现问题";
        const val PONG_ERROR = "PONG出现问题";
    }
}
