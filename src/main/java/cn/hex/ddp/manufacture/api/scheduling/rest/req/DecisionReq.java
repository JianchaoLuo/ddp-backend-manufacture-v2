package cn.hex.ddp.manufacture.api.scheduling.rest.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 决策订单列表
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
public class DecisionReq {
    @NotEmpty(message = "决策订单列表不能为空")
    List<OrderInfoReq> newOrders;
}
