package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 出库区的仿真输入
 * @Author: KangHong
 * @Created: 2024/5/30 18:01
 */
@Data
@AllArgsConstructor
public class OutBoundSimulationInput implements Serializable {
    /**
     * 出库区的母车
     */
    Ferry outboundFerry;
    /**
     * 出库区的子车
     */
    List<SubCar> subCars;
    /**
     * 出库区的工岗
     */
    List<WorkStation> workStations;
    /**
     * 出库区的所有点位列表
     */
    List<Position> positions;
    /**
     * 出库区的轨道列表
     */
    List<Path> paths;
    /**
     * 出库区的资源标识
     */
    OutBoundIndexInput outBoundIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
    /**
     * 产品需要出库的顺序队列
     */
    Deque<Product> productDeque;

    //以下为2025.1.3新增的区域输入
    Map<PositionNameEnum, Position> newPositionMap;
    //砂箱合模子车
    SubCar sandBoxMoldingSubCar;
}
