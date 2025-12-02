package cn.hex.ddp.manufacture.application.order.dto.out;

import cn.hex.ddp.manufacture.application.product.dto.out.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 订单项DTO
 *
 * @author Huhaisen
 * @date 2024/06/24
 */
@Data
@AllArgsConstructor
public class OrderItemDTO {
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
    private ProductDTO product;
}
