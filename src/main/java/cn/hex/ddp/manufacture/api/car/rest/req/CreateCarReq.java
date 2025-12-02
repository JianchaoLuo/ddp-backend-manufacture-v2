package cn.hex.ddp.manufacture.api.car.rest.req;

import cn.hex.ddp.manufacture.domain.car.enums.CarControlStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 创建车辆请求参数
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Data
public class  CreateCarReq {
    /**
     * 车辆唯一编号
     */
    @Length(max = 255, message = "车辆唯一编号长度不能超过255个字符")
    private String carNo;

    /**
     * 车辆名称
     */
    @NotEmpty(message = "车辆名称不能为空")
    @Length(max = 255, message = "车辆名称长度不能超过255个字符")
    private String name;

    /**
     * 车辆类型
     */
    @NotNull(message = "车辆类型不能为空")
    private CarTypeEnum type;

    /**
     * 车辆运行状态
     */
    private CarOperationStatusEnum operationStatus;

    /**
     * 车辆控制状态
     */
    private CarControlStatusEnum controlStatus;

    /**
     * 所属区域
     */
    private AreaEnum area;

    /**
     * 车辆型号ID
     */
    @NotNull(message = "车辆型号ID不能为空")
    private Long carModelId;

    /**
     * 所属路径ID列表
     */
    private List<Long> pathIds;
    /**
     * 车头朝向
     */
    private RailDirectionEnum headDirection;

    /**
     * 所绑定的车辆ID
     */
    private Long bindCarId;

    /**
     * 车辆名字枚举（供算法使用）
     */
    private CarNameEnum nameEnum;
}
