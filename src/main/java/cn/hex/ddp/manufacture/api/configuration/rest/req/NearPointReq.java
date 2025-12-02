package cn.hex.ddp.manufacture.api.configuration.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 获取目标点位周围的点位列表，工岗，设备，交互点
 * @author 冯泽邦
 * @date 2025/7/28
 */
@Data
public class NearPointReq {
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
