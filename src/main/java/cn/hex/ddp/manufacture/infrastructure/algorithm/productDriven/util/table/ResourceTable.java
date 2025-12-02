package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 所有资源的集合表，用于维护资源的状态信息
 * @Author: KangHong
 * @Created: 2024/11/20 20:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceTable implements Serializable {
    /**
     * 子车资源集合
     */
    private List<SubCar> subCars;
    /**
     * 母车资源集合
     */
    private List<Ferry> ferries;
    /**
     * 点位资源集合
     */
    private List<Position> positions;
    /**
     * 工岗资源集合
     */
    private List<WorkStation> workStations;
    /**
     * 设备资源集合
     */
    private List<Equipment> equipments;
    /**
     * 道路资源集合
     */
    private List<Path> paths;

    /**
     * 根据车辆的名字枚举查找子车
     * 名字枚举区分子母车可以通过枚举code的大小区分
     * @param nameEnum 车辆名字枚举
     * @return 返回子车信息
     */
    public SubCar getSubCarByName(CarNameEnum nameEnum) {

        return null;
    }

    /**
     * 根据车辆的名字枚举查找母车
     * 名字枚举区分子母车可以通过枚举code的大小区分
     * @param nameEnum 车辆名字枚举
     * @return 返回母车信息
     */
    public Ferry getFerryByName(CarNameEnum nameEnum) {

        return null;
    }

    /**
     * 根据点位的名字枚举查找母车
     * @param nameEnum 点位名字枚举
     * @return 返回点位信息
     */
    public Position getPositionByName(PositionNameEnum nameEnum) {

        return null;
    }

    /**
     * 根据工岗的名字枚举查找母车
     * @param nameEnum 工岗名字枚举
     * @return 返回工岗信息
     */
    public WorkStation getWorkStationByName(WorkstationNameEnum nameEnum) {

        return null;
    }

    /**
     * 根据设置的名字枚举查找母车
     * @param nameEnum 设置名字枚举
     * @return 返回设置信息
     */
    public Equipment getEquipmentByName(EquipmentNameEnum nameEnum) {

        return null;
    }

    /**
     * 根据道路的名字枚举查找母车
     * @param nameEnum 道路名字枚举
     * @return 返回道路信息
     */
    public Path getPathByName(PathNameEnum nameEnum) {

        return null;
    }
}
