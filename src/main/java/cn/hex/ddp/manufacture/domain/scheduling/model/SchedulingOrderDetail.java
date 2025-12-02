package cn.hex.ddp.manufacture.domain.scheduling.model;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 排产订单详情表
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Data
public class SchedulingOrderDetail {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 排产计划ID
     */
    private Long schedulingPlanId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 排产产品id
     */
    private Long productionItemId;

    /**
     * 生产数量
     */
    private Integer productionCount;

    /**
     * 订单项状态
     */
    private ProductionProgressStatusEnum productionProgress;

    /**
     * 计划类型
     */
    private PLanType planType;
}
