package cn.hex.ddp.manufacture.application.configuration.dto.out;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import lombok.Data;

/**
 * PositionDTO
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Data
public class PositionDTO {
    /**
     * 点位ID(数据库ID)
     */
    private Long id;

    /**
     * 坐标
     */
    private CoordinateDTO coordinate;

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
