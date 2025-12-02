package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Path;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Position;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoolingSimulationInput implements Serializable {
    /**
     * 冷却完成前往开箱轨道母车
     */
    Ferry coolingFerry;
    /**
     * 冷却完成前往开箱轨道子车
     */
    SubCar coolingSubCar;
    /**
     * 上冷却区点位列表
     */
    List<List<Position>> upPositions;
    /**
     * 下冷却点位列表
     */
    List<List<Position>> downPositions;
    /**
     * 冷却完成前往开箱轨道
     */
    Path coolingGoOpenBoxPath;
    /**
     * 上冷却区轨道1
     */
    Path coolingUpPath_1;
    /**
     * 上冷却区轨道2
     */
    Path coolingUpPath_2;
    /**
     * 上冷却轨道3
     */
    Path coolingUpPath_3;
    /**
     * 上冷却轨道4
     */
    Path coolingUpPath_4;
    /**
     * 上冷却轨道5
     */
    Path coolingUpPath_5;
    /**
     * 下冷却轨道1
     */
    Path coolingDownPath_1;
    /**
     * 下冷却轨道2
     */
    Path coolingDownPath_2;
    /**
     * 下冷却轨道3
     */
    Path coolingDownPath_3;
    /**
     * 下冷却轨道4
     */
    Path coolingDownPath_4;
    /**
     * 下冷却轨道5
     */
    Path coolingDownPath_5;
    /**
     * 冷却区的输入标识
     */
    CoolingIndexInput coolingIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
