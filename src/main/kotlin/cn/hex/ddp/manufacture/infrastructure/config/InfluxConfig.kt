package cn.hex.ddp.manufacture.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
open class InfluxConfig {
    @Value("\${influx.url}")
    lateinit var url: String

    @Value("\${influx.token}")
    lateinit var token: String

    @Value("\${influx.org}")
    lateinit var org: String

    @Value("\${influx.bucket}")
    lateinit var bucket: String

}
