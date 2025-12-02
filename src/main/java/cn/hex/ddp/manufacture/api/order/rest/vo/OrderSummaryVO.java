package cn.hex.ddp.manufacture.api.order.rest.vo;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单概要VO
 *
 * @author Huhaisen
 * @date 2024/09/19
 */
@Data
public class OrderSummaryVO {
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
     * 订单项列表
     */
    private List<OrderItemVO> orderItems;


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
     * 交付截止时间
     */
    private LocalDateTime deliveryDeadline;

    /**
     * 订单优先级
     */
    private Integer priority;
}
