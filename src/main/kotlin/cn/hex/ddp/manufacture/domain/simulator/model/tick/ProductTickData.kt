package cn.hex.ddp.manufacture.domain.simulator.model.tick

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum

data class ProductTickData(
    override val tick: Long,
    val id: Long,
    val mission: Long,
    val name: String,
    val productType: ProductTypeEnum,
    var productAfoot: ProductAfootEnum
) : ITickData
