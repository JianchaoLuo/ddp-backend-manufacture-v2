package cn.hex.ddp.manufacture.api.configuration.rest.req;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 批量添加点位请求参数
 *
 * @author Huhaisen
 * @date 2024/05/14
 */
@Data
public class AddPositionReq {

    /**
     * 关联的坐标ID
     */
    @NotNull(message = "坐标ID不能为空")
    private Long coordinateId;

    /**
     * 点位名称
     */
    @NotEmpty(message = "点位名称不能为空")
    @Length(max = 255, message = "点位名称长度不能超过255")
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
