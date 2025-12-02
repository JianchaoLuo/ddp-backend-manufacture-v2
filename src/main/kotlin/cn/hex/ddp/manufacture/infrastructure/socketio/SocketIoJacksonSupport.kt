package cn.hex.ddp.manufacture.infrastructure.socketio

import cn.hex.ddp.manufacture.infrastructure.common.jsckson.LongModule
import com.corundumstudio.socketio.protocol.JacksonJsonSupport
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SocketIoJacksonSupport : JacksonJsonSupport(JavaTimeModule(),LongModule()) {
    override fun init(objectMapper: ObjectMapper) {
        super.init(objectMapper)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val timeModule = JavaTimeModule()
        timeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(formatter))
        timeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(formatter))
        objectMapper.registerModule(timeModule)
    }
}
