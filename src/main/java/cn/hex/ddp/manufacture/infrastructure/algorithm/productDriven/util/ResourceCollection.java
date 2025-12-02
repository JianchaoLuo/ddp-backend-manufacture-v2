package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 事件包含的资源集合
 * @Author: KangHong
 * @Created: 2024/11/9 16:34
 */
@Data
@AllArgsConstructor
public class ResourceCollection implements Serializable {
    /**
     * 车辆资源集合
     */
    private List<CarNameEnum> carResources;
    /**
     * 点位资源集合
     */
    private List<PositionNameEnum> positionResources;
    /**
     * 工岗资源集合
     */
    private List<WorkstationNameEnum> workStationResources;
    /**
     * 设备资源集合
     */
    private List<EquipmentNameEnum> equipmentResources;
    /**
     * 道路资源集合
     */
    private List<PathNameEnum> pathResources;
}
