package cn.hex.ddp.manufacture.api.configuration.rest.vo;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import lombok.Data;

/**
 * PositionVO
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Data
public class PositionVO {
    /**
     * 点位ID(数据库ID)
     */
    private String id;

    /**
     * 坐标
     */
    private CoordinateVO coordinate;

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
