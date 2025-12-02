package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Path;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Position;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.WaitingPourIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 6:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingPourSimulationInput implements Serializable {
    /**
     * 合箱完成前往浇筑轨道母车
     */
    Ferry closingGoPourFerry;
    /**
     * 合箱完成前往浇筑轨道子车
     */
    SubCar closingGoPourSubCar;
    /**
     * 上待浇筑区点位列表
     */
    List<List<Position>> upPositions;
    /**
     * 下待浇筑区点位列表
     */
    List<List<Position>> downPositions;
    /**
     * 合箱完成前往浇筑轨道
     */
    Path closingGoPourPath;
    /**
     * 上待浇筑区轨道1
     */
    Path waitingPourUpPath_1;
    /**
     * 上待浇筑区轨道2
     */
    Path waitingPourUpPath_2;
    /**
     * 上待浇筑区轨道3
     */
    Path waitingPourUpPath_3;
    /**
     * 上待浇筑区轨道4
     */
    Path waitingPourUpPath_4;
    /**
     * 上待浇筑区轨道5
     */
    Path waitingPourUpPath_5;
    /**
     * 下待浇筑区轨道1
     */
    Path waitingPourDownPath_1;
    /**
     * 下待浇筑区轨道2
     */
    Path waitingPourDownPath_2;
    /**
     * 下待浇筑区轨道3
     */
    Path waitingPourDownPath_3;
    /**
     * 下待浇筑区轨道4
     */
    Path waitingPourDownPath_4;
    /**
     * 下待浇筑区轨道5
     */
    Path waitingPourDownPath_5;
    /**
     * 待浇筑区的输入标识
     */
    WaitingPourIndexInput waitingPourIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
