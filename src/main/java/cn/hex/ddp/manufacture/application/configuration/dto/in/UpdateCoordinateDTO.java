package cn.hex.ddp.manufacture.application.configuration.dto.in;

import lombok.Data;

/**
 * 更新坐标DTO
 *
 * @author Huhaisen
 * @date 2024/05/13
 */
@Data
public class UpdateCoordinateDTO {

    /**
     * 坐标ID
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
