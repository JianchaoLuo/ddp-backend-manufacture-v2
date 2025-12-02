package cn.hex.ddp.manufacture.api.scheduling.rest.vo;

import lombok.Data;

import java.util.List;

/**
 * 决策结果VO
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
public class DecisionResultVO {
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 操作结果消息
     */
    private String message;

    /**
     * 失败订单信息列表，仅当成功为false时返回
     */
    private List<FailedOrderInfoVO> failedOrders;
    /**
     * 创建成功的操作结果
     * @return ScheduleOperationResultVO
     */
    public static SchedulingResultVO success() {
        SchedulingResultVO result = new SchedulingResultVO();
        result.setMessage("[排产]操作成功");
        result.setSuccess(true);
        return result;
    }

    /**
     * 创建带消息的成功操作结果
     * @param message 成功消息
     * @return ScheduleOperationResultVO
     */
    public static SchedulingResultVO success(String message) {
        SchedulingResultVO result = new SchedulingResultVO();
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }

    public static SchedulingResultVO failure(String  message) {
        SchedulingResultVO result = new SchedulingResultVO();
        result.setSuccess(false);
        result.setMessage(message);
        result.setFailedOrders(null);
        return result;
    }

    public static SchedulingResultVO failure(String  message, List<FailedOrderInfoVO> failedOrders) {
        SchedulingResultVO result = new SchedulingResultVO();
        result.setSuccess(false);
        result.setMessage(message);
        result.setFailedOrders(failedOrders);
        return result;
    }
}
