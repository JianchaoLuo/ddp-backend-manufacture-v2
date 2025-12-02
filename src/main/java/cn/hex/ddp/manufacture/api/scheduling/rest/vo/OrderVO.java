package cn.hex.ddp.manufacture.api.scheduling.rest.vo;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.order.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单VO
 * @Author: FengZebang
 * @Date: 2025/7/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {
    /**
     * 订单ID(数据库ID)
     */
    private Long id;

    /**
     * 销售订单编号
     */
    private String orderNo;

    /**
     * 产品数量
     */
    private Integer productCount;

    /**
     * 订单信息(订单描述)
     */
    private String orderInfo;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;

    /**
     * 预计投产时间
     */
    private LocalDateTime expectedProductionTime;

    /**
     * 预计交付时间
     */
    private LocalDateTime expectedDeliveryTime;

    /**
     * 交付截止时间
     */
    private LocalDateTime deliveryDeadline;

    /**
     * 订单优先级
     */
    private Integer priority;
}
