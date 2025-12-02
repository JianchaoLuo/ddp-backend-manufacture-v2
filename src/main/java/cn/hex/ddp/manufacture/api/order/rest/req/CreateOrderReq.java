package cn.hex.ddp.manufacture.api.order.rest.req;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateOrderReq {
    /**
     * 销售订单编号
     */
    @NotEmpty(message = "销售订单编号不能为空")
    @Length(max = 63, message = "销售订单编号长度不能超过63")
    private String orderNo;

    /**
     * 订单信息(订单描述)
     */
    @NotEmpty(message = "订单描述信息不能为空")
    @Length(max = 512, message = "订单描述信息长度不能超过512")
    private String orderInfo;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;

    /**
     *
     * 预计投产时间
     */
    @NotNull(message = "预计投产时间不能为空")
    private LocalDateTime expectedProductionTime;

    /**
     * 交付截止时间
     */
    @NotNull(message = "交付截止时间不能为空")
    private LocalDateTime deliveryDeadline;

    /**
     * 订单优先级
     */
    private Integer priority;

    /**
     * 订单项列表
     */
    @NotEmpty(message = "订单项列表不能为空")
    List<AddOrderItemReq> orderItems;
}
