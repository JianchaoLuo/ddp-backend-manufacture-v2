package cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.ResourceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassDescription: 资源状态类，存储智能工厂的资源数据和状态
 * @Author: KangHong
 * @Created: 2024/12/7 21:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    /**
     * 车辆资源Map
     * key为车辆的名字枚举，value为车辆当前状态（空闲/忙碌）
     * 以下类似
     */
    private Map<CarNameEnum, ResourceState> carResMap;
    /**
     * 点位资源Map
     */
    private Map<PositionNameEnum, ResourceState> positionResMap;
    /**
     * 工岗资源Map
     */
    private Map<WorkstationNameEnum, ResourceState> workstationResMap;
    /**
     * 设备资源Map
     */
    private Map<EquipmentNameEnum, ResourceState> equipmentResMap;
    /**
     * 道路资源Map
     */
    private Map<PathNameEnum, ResourceState> pathResMap;
}
