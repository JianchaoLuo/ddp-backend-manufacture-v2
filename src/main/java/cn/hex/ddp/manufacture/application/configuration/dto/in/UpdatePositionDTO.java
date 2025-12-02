package cn.hex.ddp.manufacture.application.configuration.dto.in;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import lombok.Data;

/**
 * 更新点位信息DTO
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Data
public class UpdatePositionDTO {
    /**
     * 点位ID
     */
    private Long id;

    /**
     * 关联的坐标ID
     */
    private Long coordinateId;

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
