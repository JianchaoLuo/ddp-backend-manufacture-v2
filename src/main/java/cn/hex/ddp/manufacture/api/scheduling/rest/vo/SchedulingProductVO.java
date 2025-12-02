package cn.hex.ddp.manufacture.api.scheduling.rest.vo;

import cn.hex.ddp.manufacture.api.product.rest.vo.ProductVO;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import lombok.Data;

/**
 * 排产产品VO
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
public class SchedulingProductVO {
    /**
     * 排产产品ID
     */
    private Long id;

    /**
     * 产品 信息
     */
    private Product product;

    /**
     * 订单 信息
     */
    private Order order;

    /**
     * 生产数量
     */
    private Integer productionCount;

}
