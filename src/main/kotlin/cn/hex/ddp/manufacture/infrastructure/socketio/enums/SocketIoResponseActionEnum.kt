package cn.hex.ddp.manufacture.infrastructure.socketio.enums

enum class SocketIoResponseActionEnum(
    val type: Int,
    val desc: String
) {
    ERROR(-1, "错误")
}
