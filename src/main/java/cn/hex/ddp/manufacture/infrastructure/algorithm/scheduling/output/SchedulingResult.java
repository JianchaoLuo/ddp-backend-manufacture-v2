package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output;

/**
 * 新排产算法输出类
 * @author LiuYongkang
 * @date 2025/8/9
 * @Description
 */

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 排产算法的统一返回结果。
 * 用于封装成功和失败两种情况，使调用方能明确处理。
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulingResult {

    /**
     * 标识排产是否成功。
     */
    private boolean success;

    /**
     * 排产成功时的结果数据。
     * 如果 success = false，此字段为 null。
     */
    private SchedulingOutPut successOutput;

    /**
     * 排产失败时，导致失败的订单列表。
     * 如果 success = true，此字段为 null 或为空。
     */
    private List<FailedOrderInfo> failedOrders;

    /**
     * 创建一个表示成功的静态工厂方法。
     * @param output 成功的排产结果
     * @return 包含成功结果的 SchedulingResult 实例
     */
    public static SchedulingResult success(SchedulingOutPut output) {
        SchedulingResult result = new SchedulingResult();
        result.setSuccess(true);
        result.setSuccessOutput(output);
        return result;
    }

    /**
     * 创建一个表示失败的静态工厂方法。
     * @param failedOrders 导致失败的订单列表
     * @return 包含失败信息的 SchedulingResult 实例
     */
    public static SchedulingResult failure(List<FailedOrderInfo> failedOrders) {
        SchedulingResult result = new SchedulingResult();
        result.setSuccess(false);
        result.setFailedOrders(failedOrders);
        return result;
    }

    /**
     * 内部类，用于封装失败订单的简要信息。
     */
    @Data
    @AllArgsConstructor
    public static class FailedOrderInfo {
        private Long orderId;
        private String reason; // 可以添加失败原因，例如 "无法满足交期"
    }
}
