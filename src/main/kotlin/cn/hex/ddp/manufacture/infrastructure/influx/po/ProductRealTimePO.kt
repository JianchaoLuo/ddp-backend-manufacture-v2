package cn.hex.ddp.manufacture.infrastructure.influx.po

import cn.hex.ddp.manufacture.domain.simulator.model.tick.ProductTickData
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum
import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement

@Measurement(name = "product_real_time")
data class ProductRealTimePO(
    @Column(name = "tick")
    val tick: Long = 0L,
    @Column(name = "id", tag = true)
    val id: String = "",
    @Column(name = "mission", tag = true)
    val mission: String = "",
    @Column(name = "name")
    val name: String = "",
    @Column(name = "productType")
    val productType: String = "",
    @Column(name = "productAfoot")
    val productAfoot: String = "",
) {
    fun toProductTickData(): ProductTickData {
        return ProductTickData(
            tick = tick,
            id = id.toLong(),
            mission = mission.toLong(),
            name = name,
            productType = ProductTypeEnum.valueOf(productType),
            productAfoot = ProductAfootEnum.valueOf(productAfoot)
        )
    }
}
