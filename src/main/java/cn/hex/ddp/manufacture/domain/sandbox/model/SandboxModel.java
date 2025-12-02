package cn.hex.ddp.manufacture.domain.sandbox.model;

import cn.hex.ddp.manufacture.domain.sandbox.enums.SandboxTypeEnum;
import lombok.Data;

/**
 * 砂箱型号模型
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Data
public class SandboxModel {
    /**
     * 砂箱型号ID(数据库ID)
     */
    private Long id;

    /**
     * 砂箱型号名
     */
    private String modelName;

    /**
     * 砂箱长度
     */
    private Float length;

    /**
     * 砂箱宽度
     */
    private Float width;

    /**
     * 砂箱高度
     */
    private Float height;

    /**
     * 砂箱空重
     */
    private Float emptyWeight;

    /**
     * 砂箱满重
     */
    private Float fullWeight;

    /**
     * 砂箱位置
     * 1代表最上层，2代表第二层,...
     */
    private Integer layer;

    /**
     * 砂箱冷热类型
     */
    private SandboxTypeEnum type;

}
