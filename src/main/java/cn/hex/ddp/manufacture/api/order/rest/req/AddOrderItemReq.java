package cn.hex.ddp.manufacture.api.order.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrderItemReq {
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
