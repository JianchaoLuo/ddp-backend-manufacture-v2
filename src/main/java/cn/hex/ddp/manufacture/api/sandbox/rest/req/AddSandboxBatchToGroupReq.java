package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddSandboxBatchToGroupReq {

    /**
     * 砂箱ID
     */
    @NotNull(message = "砂箱ID不能为空")
    private Long sandboxId;

    /**
     * 砂箱层级
     * 1代表最上层，2代表第二层,..
     */
    @NotNull(message = "砂箱层级不能为空")
    private Integer layer;

    /**
     * 优先级
     */
    private Integer priority;
}
