package cn.hex.ddp.manufacture.api.order.rest.vo;

import cn.hex.ddp.manufacture.domain.product.model.Product;
import lombok.Data;

/**
 * 订单项VO
 *
 * @author Huhaisen
 * @date 2024/09/19
 */
@Data
public class OrderItemVO {
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
     * 产品
     */
    private Product product;
}
