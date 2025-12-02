package cn.hex.ddp.manufacture.domain.product.model;

import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import lombok.Data;

/**
 * 排产产品实例
 * 产品与排产产品是一对多关系
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Data
public class ProductionItem {
    /**
     * id
     */
    private Long id;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 生产数量
     */
    private Integer productionCount;

    /**
     * 计划类型
     */
    PLanType planType;
}
