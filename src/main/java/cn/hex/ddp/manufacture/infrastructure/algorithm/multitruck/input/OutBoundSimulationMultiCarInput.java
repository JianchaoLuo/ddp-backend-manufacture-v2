package cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.multiCar.OutBoundIndexInputMultiCar;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Deque;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/10/29 16:09
 */
@Data
@AllArgsConstructor
public class OutBoundSimulationMultiCarInput implements Serializable {
    /**
     * 出库区的母车
     * 新增车辆
     */
    Ferry outboundFerryHot;
    /**
     * 出库区的母车
     * 新增车辆
     */
    Ferry outboundFerryCold;
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
    OutBoundIndexInputMultiCar outBoundIndexInputMultiCar;
    /**
     * 整体的总时间
     */
    double totalTime;
    /**
     * 产品需要出库的顺序队列
     */
    Deque<Product> productDeque;
}
