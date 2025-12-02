package cn.hex.ddp.manufacture.api.order.rest.req;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateOrderReq {
    /**
     * 销售订单编号
     */
    @Length(max = 63, message = "销售订单编号长度不能超过63")
    private String orderNo;

    /**
     * 订单信息(订单描述)
     */
    @Length(max = 512, message = "订单描述信息长度不能超过512")
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

    /**
     * 订单项列表
     */
    List<AddOrderItemReq> orderItems;
}
