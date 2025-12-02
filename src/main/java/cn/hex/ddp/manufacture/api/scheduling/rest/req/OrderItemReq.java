package cn.hex.ddp.manufacture.api.scheduling.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 预计新增订单中的产品项
 * @author 冯泽邦
 * @date 2025/8/9
 */
@Data
public class OrderItemReq {
    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    /**
     * 生产数量
     */
    @NotNull(message = "生产数量不能为空")
    private Integer count;
}
