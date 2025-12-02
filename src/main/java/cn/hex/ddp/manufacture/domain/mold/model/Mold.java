package cn.hex.ddp.manufacture.domain.mold.model;

import lombok.Data;

/**
 * 模具模型
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class Mold {
    /**
     * 模具ID(数据库ID)
     */
    private Long id;

    /**
     * 模具编号
     * 每个模具实体的唯一编号
     */
    private String moldNo;

    /**
     * 模具名称
     */
    private String name;

    /**
     * 模具型号
     */
    private MoldModel moldModel;

    /**
     * 模具状态
     * 有没有被使用过之类的，是否可用...
     */
    private String status;

}
