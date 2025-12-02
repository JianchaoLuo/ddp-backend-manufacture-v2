package cn.hex.ddp.manufacture.domain.configuration.model;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import lombok.Data;

/**
 * 点位聚合模型
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Data
public class Position {
    /**
     * 点位ID(数据库ID)
     */
    private Long id;

    /**
     * 坐标
     */
    private Coordinate coordinate;

    /**
     * 点位名称
     */
    private String name;
    /**
     * 点位描述
     */
    private String description;

    /**
     * 点位名称枚举(供算法使用)
     */
    private PositionNameEnum nameEnum;

}
