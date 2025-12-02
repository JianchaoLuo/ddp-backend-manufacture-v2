package cn.hex.ddp.manufacture.domain.product.model;

import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排产产品
 * 为了维护每个产品的生产进度，因此添加这个表
 * @Author: FengZebang
 * @Date: 2025/5/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledProduct {
    /**
     * 排产产品id（数据库id）
     */
    private Long id;

    /**
     * 产品id（标明所属产品种类）
     */
    private Long productId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 生产进度（当前完成的工序）
     */
    private ProductionProgressStatusEnum  productionProgress;

    /**
     * 产品
     * 关联查询
     */
    private Product product;

    /**
     * 订单
     * 订单关联查询
     */
    private Order order;

}
