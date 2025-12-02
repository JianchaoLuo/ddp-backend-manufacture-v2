package cn.hex.ddp.manufacture.application.mold.dto.out;

import lombok.Data;

@Data
public class MoldGroupMoldSummaryDTO {

    /**
     * 模具ID
     */
    private Long moldId;

    /**
     * 层级
     * 1代表最上层，2代表第二层,..
     */
    private Integer layer;

    /**
     * 优先级
     */
    private Integer priority;
}
