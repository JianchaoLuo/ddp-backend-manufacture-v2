package cn.hex.ddp.manufacture.domain.configuration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 坐标模型
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {
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

    public Coordinate(Float x, Float y) {
        this.x = x;
        this.y = y;
    }
}
