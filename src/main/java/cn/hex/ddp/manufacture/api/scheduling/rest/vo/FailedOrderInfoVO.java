package cn.hex.ddp.manufacture.api.scheduling.rest.vo;

import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 失败订单信息
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailedOrderInfoVO {
    /**
     * 订单视图
     */
    private OrderVO order;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 从SchedulingResult.FailedOrderInfo创建FailedOrderInfoVO
     * @param failedOrderInfo SchedulingResult.FailedOrderInfo对象
     * @return FailedOrderInfoVO对象
     */
    public static FailedOrderInfoVO from(SchedulingResult.FailedOrderInfo failedOrderInfo) {
        if (failedOrderInfo == null) {
            return null;
        }
        // 创建OrderVO对象并设置订单ID
        OrderVO orderVO = new OrderVO();
        orderVO.setId(failedOrderInfo.getOrderId());

        return new FailedOrderInfoVO(orderVO, failedOrderInfo.getReason());
    }

    /**
     * 从SchedulingResult.FailedOrderInfo列表创建FailedOrderInfoVO列表
     * @param failedOrderInfos SchedulingResult.FailedOrderInfo对象列表
     * @return FailedOrderInfoVO对象列表
     */
    public static List<FailedOrderInfoVO> fromList(List<SchedulingResult.FailedOrderInfo> failedOrderInfos) {
        if (failedOrderInfos == null) {
            return null;
        }
        return failedOrderInfos.stream()
                .map(FailedOrderInfoVO::from)
                .collect(Collectors.toList());
    }
}
