package cn.hex.ddp.manufacture.domain.mold.model;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import lombok.Data;

/**
 * 模具型号模型
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class MoldModel {
    /**
     * 模具型号ID(数据库ID)
     */
    private Long id;

    /**
     * 模具型号名
     */
    private String modelName;

    /**
     * 模具类型
     */
    private MoldTypeEnum type;

    /**
     * 模具位置
     * 1代表最上层，2代表第二层,....
     */
    private Integer layer;

    /**
     * 模具长度
     */
    private Float length;

    /**
     * 模具宽度
     */
    private Float width;

    /**
     * 模具高度
     */
    private Float height;

    /**
     * 模具重量
     */
    private Float weight;

}
