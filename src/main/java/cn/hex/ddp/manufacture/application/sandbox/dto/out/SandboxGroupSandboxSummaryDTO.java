package cn.hex.ddp.manufacture.application.sandbox.dto.out;

import lombok.Data;

@Data
public class SandboxGroupSandboxSummaryDTO {
    /**
     * 砂箱ID
     */
    private Long sandboxId;

    /**
     * 砂箱层级
     * 1代表最上层，2代表第二层,..
     */
    private Integer layer;

    /**
     * 优先级
     */
    private Integer priority;
}
