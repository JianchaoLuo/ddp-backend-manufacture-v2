package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bobo
 * 算法包下的排产产品类，用于向后端提交算法结果
 * 2025/5/29 16:47
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingProduct {

    /**
     * 排产产品的id
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
     * 产品当前的生产流程
     */
    private ProductionProgressStatusEnum productionProgress;

}
