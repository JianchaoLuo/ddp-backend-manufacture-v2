package cn.hex.ddp.manufacture.api.scheduling.rest.vo;

import cn.hex.ddp.manufacture.api.order.rest.vo.OrderItemVO;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排产订单VO
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
public class SchedulingOrderVO {
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
     * 预计交付
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
