package cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author bobo
 * @date 2025/6/6 11:16
 */
@Data
@AllArgsConstructor
public class ScheduleProductInput {
    /**
     * 排产产品id
     */
    private Long id;
    /**
     * 产品型号id（标明所属产品种类）
     */
    private Long productId;
    /**
     * 产品来源的订单的id（订单id，不是排产订单id）
     */
    private Long orderId;
}
