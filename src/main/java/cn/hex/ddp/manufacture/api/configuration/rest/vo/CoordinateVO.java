package cn.hex.ddp.manufacture.api.configuration.rest.vo;

import lombok.Data;

/**
 * 坐标VO
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Data
public class CoordinateVO {
    /**
     * 坐标ID(数据库ID)
     */
    private String id;

    /**
     * 横坐标
     */
    private Float x;

    /**
     * 纵坐标
     */
    private Float y;
}
