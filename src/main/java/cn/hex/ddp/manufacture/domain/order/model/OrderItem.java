package cn.hex.ddp.manufacture.domain.order.model;

import lombok.Data;

/**
 * 订单项模型
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Data
public class OrderItem {

    /**
     * 订单项ID(数据库主键ID)
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 生产数量
     */
    private Integer count;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 完成数量
     */
    private Integer completedCount;
}
