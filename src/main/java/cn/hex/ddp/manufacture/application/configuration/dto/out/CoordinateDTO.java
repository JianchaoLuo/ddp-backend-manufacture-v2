package cn.hex.ddp.manufacture.application.configuration.dto.out;

import lombok.Data;

/**
 * 坐标DTO
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Data
public class CoordinateDTO {
    /**
     * 坐标ID(数据库ID)
     */
    private Long id;

    /**
     * 横坐标
     */
    private Float x;

    /**
     * 纵坐标
     */
    private Float y;
}
