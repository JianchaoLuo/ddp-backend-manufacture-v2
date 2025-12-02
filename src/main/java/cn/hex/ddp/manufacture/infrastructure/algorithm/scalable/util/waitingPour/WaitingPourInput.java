package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.waitingPour;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription: 待浇筑区资源集合
 * @Author: KangHong
 * @Created: 2024/11/28 15:50
 */
@Data
@AllArgsConstructor
public class WaitingPourInput {
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
