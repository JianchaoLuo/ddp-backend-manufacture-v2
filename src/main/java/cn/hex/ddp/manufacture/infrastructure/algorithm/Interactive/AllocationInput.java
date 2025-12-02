package cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 工厂的基础配置信息输入
 * @Author: KangHong
 * @Created: 2024/6/1 20:44
 */
@Data
@AllArgsConstructor
public class AllocationInput implements Serializable {
    /**
     * 子车列表
     */
    private List<SubCar> subCars;
    /**
     * 母车列表
     */
    private List<Ferry> ferries;
    /**
     * 设备列表
     */
    private List<Equipment> equipments;
    /**
     * 点位列表
     */
    private List<Position> positions;
    /**
     * 工岗列表
     */
    private List<WorkStation> workStations;
    /**
     * 轨道列表
     */
    private List<Path> paths;
    /**
     * 所属区域
     */
    private AreaEnum region;
}
