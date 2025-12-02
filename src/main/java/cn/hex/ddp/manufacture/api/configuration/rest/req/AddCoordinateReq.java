package cn.hex.ddp.manufacture.api.configuration.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加坐标请求参数
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Data
public class AddCoordinateReq {
    /**
     * 横坐标
     */
    @NotNull(message = "横坐标不能为空")
    private Float x;

    /**
     * 纵坐标
     */
    @NotNull(message = "纵坐标不能为空")
    private Float y;
}
