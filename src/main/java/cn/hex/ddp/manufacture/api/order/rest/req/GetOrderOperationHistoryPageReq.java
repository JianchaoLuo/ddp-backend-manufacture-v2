package cn.hex.ddp.manufacture.api.order.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.order.enums.OrderOperationTypeEnum;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetOrderOperationHistoryPageReq extends PageReq {
    /**
     * 订单ID（条件查询）
     */
    private Long orderId;

    /**
     * 订单操作类型（条件查询）
     */
    private OrderOperationTypeEnum operationType;
}
