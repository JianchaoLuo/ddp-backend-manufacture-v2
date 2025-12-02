package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 资源类，用于存放事件结束后资源的状态信息
 * @Author: KangHong
 * @Created: 2024/11/22 17:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource implements Serializable {
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
}
