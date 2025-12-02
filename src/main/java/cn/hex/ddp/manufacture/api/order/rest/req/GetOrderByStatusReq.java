package cn.hex.ddp.manufacture.api.order.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 根据订单状态查询订单请求参数
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
public class GetOrderByStatusReq extends PageReq {
    /**
     * 订单状态
     */
    @NotEmpty
    private OrderStatusEnum orderStatus;
}
