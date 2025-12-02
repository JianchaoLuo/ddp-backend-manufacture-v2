package cn.hex.ddp.manufacture.domain.sandbox.model;

import lombok.Data;

/**
 * 砂箱模型
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Data
public class Sandbox {
    /**
     * 砂箱ID(数据库ID)
     */
    private Long id;

    /**
     * 砂箱编号
     * 每个单体砂箱的唯一物理编码
     */
    private String sandboxNo;

    /**
     * 砂箱名称
     */
    private String name;

    /**
     * 砂箱版本
     */
    private String version;

    /**
     * 砂箱型号
     */
    private SandboxModel sandboxModel;

    /**
     * 砂箱状态
     * 有没有被使用过之类的，是否可用...
     */
    private String status;

}
