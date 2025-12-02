package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.multiCar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/10/29 16:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutBoundIndexInputMultiCar {
    /**
     * 出库轨道热砂子车的标识
     * 新增车辆
     */
    Integer outBoundSubcarHotIndex;
    /**
     * 出库轨道冷砂子车的标识
     * 新增车辆
     */
    Integer outBoundSubcarColdIndex;
    /**
     * 砂箱回流轨道子车标识
     */
    Integer sandBoxRefluxSubcarIndex_front;
    /**
     * 砂箱回流轨道子车标识
     * 新增车辆
     */
    Integer sandBoxRefluxSubcarIndex_after;
    /**
     * 制芯轨道子车标识
     */
    Integer coreMakingSubcarIndex;
    /**
     * 模具回流轨道子车标识
     * 新增车辆
     */
    Integer moldRefluxSubcarIndex_front;
    /**
     * 模具回流轨道子车标识
     * 新增车辆
     */
    Integer moldRefluxSubcarIndex_after;
    /**
     * 冷砂回流轨道子车标识
     */
    Integer coldRefluxSubcarIndex;
    /**
     * 热砂合模交互点位标识
     */
    Integer hotSandMoldingPositionIndex;
    /**
     * 冷砂合模交互点标识
     */
    Integer coldSandMoldingPositionIndex;
    /**
     * 砂箱回流点位标识
     */
    Integer sandBoxRefluxPositionIndex;
    /**
     * 制芯点位标识
     */
    Integer coreMakingPositionIndex;
    /**
     * 制芯轨道产品放置的点位标识
     */
    Integer coreMakingLayPositionIndex;
    /**
     * 模具回流点位标识
     */
    Integer moldRefluxPositionIndex;
    /**
     * 冷砂回流点位标识
     */
    Integer coldRefluxPositionIndex;
    /**
     * 热砂回流放置点位标识
     */
    Integer hotSandBoxRefluxPositionIndex;
    /**
     * 冷热砂混合回流放置点位标识
     */
    Integer hotAndColdSandBoxRefluxPositionIndex;
    /**
     * 热砂回流垂直放置点位标识
     */
    Integer hotSandVertiaclPositionIndex;
    /**
     * 砂箱回流行车点位标识
     */
    Integer sandBoxRowCarPositionIndex;
    /**
     * 冷砂模具回流点位标识
     */
    Integer coldSandMoldRefluxPositionIndex;
    /**
     * 热砂模具回流点位标识
     */
    Integer hotSandMoldRefluxPositionIndex;
    /**
     * 冷砂行车放置点位标识
     */
    Integer coldSandRowCarPositionIndex;
    /**
     * 出库轨道标识
     */
    Integer outBoundPathIndex;
    /**
     * 砂箱回流轨道标识
     */
    Integer sandBoxRefluxPathIndex;
    /**
     * 冷砂回流轨道标识
     */
    Integer coldSandBoxRefluxPathIndex;
    /**
     * 制芯轨道标识
     */
    Integer coreMakingPathIndex;
    /**
     * 模具回流轨道标识
     */
    Integer moldRefluxPathIndex;
    /**
     * 出库轨道子车在甘特图中的标识
     * 新增车辆
     */
    Integer outBoundSubCarHotInGanttIndex;
    /**
     * 出库轨道母车在甘特图中的标识
     * 将母车的甘特图事件与子车的甘特图事件全部归于子车的甘特图事件
     * 新增车辆
     */
//    Integer outBoundFerryHotInGanttIndex;
    /**
     * 出库轨道子车在甘特图中的标识
     * 新增车辆
     */
    Integer outBoundSubCarColdInGanttIndex;
    /**
     * 出库轨道母车在甘特图中的标识
     * 将母车的甘特图事件与子车的甘特图事件全部归于子车的甘特图事件
     * 新增车辆
     */
//    Integer outBoundFerryColdInGanttIndex;
    /**
     * 砂箱回流轨道子车在甘特图中的标识
     * 新增车辆
     */
    Integer sandBoxRefluxSubcarFrontInGanttIndex;
    /**
     * 砂箱回流轨道子车在甘特图中的标识
     * 新增车辆
     */
    Integer sandBoxRefluxSubcarAfterInGanttIndex;
    /**
     * 制芯轨道子车在甘特图中的标识
     */
    Integer coreMakingSubcarInGanttIndex;
    /**
     * 模具回流轨道子车在甘特图中的标识
     * 新增车辆
     */
    Integer moldRefluxSubcarFrontInGanttIndex;
    /**
     * 模具回流轨道子车在甘特图中的标识
     * 新增车辆
     */
    Integer moldRefluxSubcarAfterInGanttIndex;
    /**
     * 冷砂回流轨道子车在甘特图中的标识
     */
    Integer coldRefluxSubcarInGanttIndex;


    Integer positionInGanttIndex;
}
