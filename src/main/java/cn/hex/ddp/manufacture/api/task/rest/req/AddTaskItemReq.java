package cn.hex.ddp.manufacture.api.task.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 添加仿真任务列表项请求参数
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Data
@AllArgsConstructor
public class AddTaskItemReq {
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 生产数量
     */
    @NotNull(message = "生产数量不能为空")
    private Integer count;

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空")
    private Long productId;
}
