package cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassDescription: 智能工程的仿真资源输入，包括车辆、岗位等；
 * @Author: KangHong
 * @Created: 2024/12/7 22:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInput {
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
        for (SubCar subCar : subCars) {
            if (subCar.getNameEnum().equals(nameEnum)) {
                return subCar;
            }
        }
        return null; //如果没有找到对应的子车，返回null
    }

    /**
     * 根据车辆的名字枚举查找母车
     * 名字枚举区分子母车可以通过枚举code的大小区分
     * @param nameEnum 车辆名字枚举
     * @return 返回母车信息
     */
    public Ferry getFerryByName(CarNameEnum nameEnum) {
        for (Ferry ferry : ferries) {
            if (ferry.getNameEnum().equals(nameEnum)) {
                return ferry;
            }
        }
        return null;
    }

    /**
     * 根据车辆的枚举名判断车辆是子车还是母车
     * 名字枚举区分子母车可以通过枚举code的大小区分
     * @param nameEnum 车辆名字枚举
     * @return 是子车返回true，是母车返回false
     */
    public boolean isSubCarByName(CarNameEnum nameEnum){
        String nameStr = nameEnum.name();
        if (nameStr.endsWith("SUB_CAR")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据点位的名字枚举查找母车
     * @param nameEnum 点位名字枚举
     * @return 返回点位信息
     */
    public Position getPositionByName(PositionNameEnum nameEnum) {
        for (Position position : positions) {
            if (position.getNameEnum().equals(nameEnum)) {
                return position;
            }
        }
        return null;
    }

    /**
     * 根据工岗的名字枚举查找母车
     * @param nameEnum 工岗名字枚举
     * @return 返回工岗信息
     */
    public WorkStation getWorkStationByName(WorkstationNameEnum nameEnum) {
        for (WorkStation workStation : workStations) {
            if (workStation.getNameEnum().equals(nameEnum)) {
                return workStation;
            }
        }
        return null;
    }

    /**
     * 根据设置的名字枚举查找母车
     * @param nameEnum 设置名字枚举
     * @return 返回设置信息
     */
    public Equipment getEquipmentByName(EquipmentNameEnum nameEnum) {
        for (Equipment equipment : equipments) {
            if (equipment.getNameEnum().equals(nameEnum)) {
                return equipment;
            }
        }
        return null;
    }

    /**
     * 根据道路的名字枚举查找母车
     * @param nameEnum 道路名字枚举
     * @return 返回道路信息
     */
    public Path getPathByName(PathNameEnum nameEnum) {
        for (Path path : paths) {
            if (path.getNameEnum().equals(nameEnum)) {
                return path;
            }
        }
        return null;
    }
}
