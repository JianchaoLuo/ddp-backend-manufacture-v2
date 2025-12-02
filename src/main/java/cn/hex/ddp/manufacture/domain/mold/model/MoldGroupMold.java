package cn.hex.ddp.manufacture.domain.mold.model;

import lombok.Data;

/**
 * 模具组与模具的关联模型
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class MoldGroupMold {

    /**
     * 模具
     */
    private Mold mold;

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
