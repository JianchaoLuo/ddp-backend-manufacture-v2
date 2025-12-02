package cn.hex.ddp.manufacture.api.order.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetOrderPageReq extends PageReq {

    /**
     * 根据销售订单编号模糊查询(不传不搜索)
     */
    private String orderNo;

    /**
     * 根据订单描述信息模糊查询(不传不搜索)
     */
    private String orderInfo;

    /**
     * 订单状态（条件查询,不传不搜索）
     */
    private OrderStatusEnum orderStatus;
}
