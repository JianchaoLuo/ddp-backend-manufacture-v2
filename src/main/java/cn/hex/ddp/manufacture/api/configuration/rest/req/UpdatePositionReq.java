package cn.hex.ddp.manufacture.api.configuration.rest.req;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 更新点位请求参数
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Data
public class UpdatePositionReq {
    /**
     * 关联的坐标ID
     */
    private Long coordinateId;

    /**
     * 点位名称
     */
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
