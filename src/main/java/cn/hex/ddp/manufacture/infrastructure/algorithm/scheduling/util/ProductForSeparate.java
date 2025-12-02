package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/6/5 下午4:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForSeparate {
    /**
     * 产品id（从排产订单输入拿）
     */
    private Long id;
    /**
     * 产品型号id
     */
    private Long productId;
    /**
     * 产品来源的订单id
     */
    private Long orderId;
    /**
     * 产品当天的生产流程
     */
    private ProductionProgressStatusEnum productionProgress;
}
