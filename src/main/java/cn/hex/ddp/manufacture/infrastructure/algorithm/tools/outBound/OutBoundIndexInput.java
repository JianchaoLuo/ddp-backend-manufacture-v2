package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription: 用来在List中找到每个资源的标识
 * @Author: KangHong
 * @Created: 2024/6/4 20:23
 */
@Data
@AllArgsConstructor
public class OutBoundIndexInput implements Serializable {
    /**
     * 出库轨道子车的标识
     */
    Integer outBoundSubcarIndex;
    /**
     * 砂箱回流轨道子车标识
     */
    Integer sandBoxRefluxSubcarIndex;
    /**
     * 制芯轨道子车标识
     */
    Integer coreMakingSubcarIndex;
    /**
     * 模具回流轨道子车标识
     */
    Integer moldRefluxSubcarIndex;
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
     */
    Integer outBoundSubCarInGanttIndex;
    /**
     * 出库轨道母车在甘特图中的标识
     */
    Integer outBoundFerryInGanttIndex;
    /**
     * 砂箱回流轨道子车在甘特图中的标识
     */
    Integer sandBoxRefluxSubcarInGanttIndex;
    /**
     * 制芯轨道子车在甘特图中的标识
     */
    Integer coreMakingSubcarInGanttIndex;
    /**
     * 模具回流轨道子车在甘特图中的标识
     */
    Integer moldRefluxSubcarInGanttIndex;
    /**
     * 冷砂回流轨道子车在甘特图中的标识
     */
    Integer coldRefluxSubcarInGanttIndex;


    Integer positionInGanttIndex;

    //新增车辆
    /**
     * 热砂合模和回流子车在甘特图中的标识
     */
    Integer hotSandMoldingSubcarInGanttIndex;

    public OutBoundIndexInput() {
    }
}
