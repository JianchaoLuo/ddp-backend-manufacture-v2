package cn.hex.ddp.manufacture.api.mold.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddMoldBatchToGroupReq {

    /**
     * 模具ID
     */
    @NotNull(message = "模具ID不能为空")
    private Long moldId;

    /**
     * 层级
     * 1代表最上层，2代表第二层,..
     */
    @NotNull(message = "模具层级不能为空")
    private Integer layer;

    /**
     * 优先级
     */
    private Integer priority;
}
