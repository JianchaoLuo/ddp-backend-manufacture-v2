package cn.hex.ddp.manufacture.domain.sandbox.model;

import lombok.Data;

/**
 * 砂箱组与砂箱关系模型
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class SandboxGroupSandbox {

    /**
     * 砂箱
     */
    private Sandbox sandbox;

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
