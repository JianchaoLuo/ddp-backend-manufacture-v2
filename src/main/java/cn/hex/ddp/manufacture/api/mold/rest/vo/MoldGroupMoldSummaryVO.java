package cn.hex.ddp.manufacture.api.mold.rest.vo;

import lombok.Data;

@Data
public class MoldGroupMoldSummaryVO {

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
