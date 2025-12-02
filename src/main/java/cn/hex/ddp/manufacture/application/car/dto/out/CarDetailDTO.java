package cn.hex.ddp.manufacture.application.car.dto.out;

import cn.hex.ddp.manufacture.domain.car.enums.CarControlStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import lombok.Data;

import java.util.List;

/**
 * 车辆详情DTO
 *
 * @author Huhaisen
 * @date 2024/06/28
 */
@Data
public class CarDetailDTO {
    /**
     * 车辆ID(数据库ID)
     */
    private Long id;

    /**
     * 车辆唯一编号
     */
    private String carNo;

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 车辆类型
     */
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
     * 车辆型号
     */
    private CarModel model;

    /**
     * 所属路径
     */
    private List<CarPathDetailDTO> carPaths;
    /**
     * 车头朝向
     */
    private RailDirectionEnum headDirection;

    /**
     * 所绑定的车辆
     */
    private CarSummaryDTO bindCar;

    /**
     * 车辆名字枚举（供算法使用）
     */
    private CarNameEnum nameEnum;
}
