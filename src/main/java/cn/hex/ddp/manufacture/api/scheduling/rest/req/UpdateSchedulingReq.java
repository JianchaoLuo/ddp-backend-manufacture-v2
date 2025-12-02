package cn.hex.ddp.manufacture.api.scheduling.rest.req;

import lombok.Data;

import java.util.List;

/**
 * 更新排产计划请求参数
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Data
public class UpdateSchedulingReq {
    /**
     * 更新的订单ID
     */
    private List<Long> orderList;
}
