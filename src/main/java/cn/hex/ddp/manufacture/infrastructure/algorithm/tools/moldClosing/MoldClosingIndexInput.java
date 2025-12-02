package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/23 15:26
 */
@Data
@AllArgsConstructor
public class MoldClosingIndexInput implements Serializable {
    /**
     * 热砂模具合模工岗标识
     */
    Integer hotMoldClosingWorkStationIndex_1;
    /**
     * 热砂模具合模工岗标识
     */
    Integer hotMoldClosingWorkStationIndex_2;
    /**
     * 热砂模具合模工岗标识
     */
    Integer hotMoldClosingWorkStationIndex_3;
    /**
     * 热砂模具合模工岗标识
     */
    Integer hotMoldClosingWorkStationIndex_4;
    /**
     * 冷砂模具合模工岗标识
     */
    Integer coldMoldClosingWorkStationIndex_1;
    /**
     * 冷砂模具合模工岗标识
     */
    Integer coldMoldClosingWorkStationIndex_2;
    /**
     * 冷砂模具合模工岗标识
     */
    Integer coldMoldClosingWorkStationIndex_3;
    /**
     * 冷砂模具合模工岗标识
     */
    Integer coldMoldClosingWorkStationIndex_4;
    /**
     * 热砂合模轨道子车在甘特图中的标识
     */
    Integer hotMoldClosingSubCarInGanttIndex;
    /**
     * 热砂合模轨道母车在甘特图中的标识
     */
    Integer hotMoldClosingFerryInGanttIndex;
    /**
     * 冷砂合模轨道子车在甘特图中的标识
     */
    Integer coldMoldClosingSubCarInGanttIndex;
    /**
     * 冷砂合模轨道母车在甘特图中的标识
     */
    Integer coldMoldClosingFerryInGanttIndex;
    /**
     * 热砂合模工岗1在甘特图中的标识
     */
    Integer hotMoldWorkStationInGanttIndex_1;
    /**
     * 热砂合模工岗2在甘特图中的标识
     */
    Integer hotMoldWorkStationInGanttIndex_2;
    /**
     * 热砂合模工岗3在甘特图中的标识
     */
    Integer hotMoldWorkStationInGanttIndex_3;
    /**
     * 热砂合模工岗4在甘特图中的标识
     */
    Integer hotMoldWorkStationInGanttIndex_4;
    /**
     * 冷砂合模工岗1在甘特图中的标识
     */
    Integer coldMoldWorkStationInGanttIndex_1;
    /**
     * 冷砂合模工岗2在甘特图中的标识
     */
    Integer coldMoldWorkStationInGanttIndex_2;
    /**
     * 冷砂合模工岗3在甘特图中的标识
     */
    Integer coldMoldWorkStationInGanttIndex_3;
    /**
     * 冷砂合模工岗4在甘特图中的标识
     */
    Integer coldMoldWorkStationInGanttIndex_4;

    public MoldClosingIndexInput() {
    }
}
