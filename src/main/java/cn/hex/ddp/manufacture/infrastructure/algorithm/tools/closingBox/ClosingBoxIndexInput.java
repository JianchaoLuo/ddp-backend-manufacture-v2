package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/20 23:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosingBoxIndexInput implements Serializable {
    /**
     * 热砂合箱工岗标识
     */
    Integer hotClosingBoxWorkStationIndex_1;
    /**
     * 热砂合箱工岗标识
     */
    Integer hotClosingBoxWorkStationIndex_2;
    /**
     * 热砂合箱工岗标识
     */
    Integer hotClosingBoxWorkStationIndex_3;
    /**
     * 热砂合箱工岗标识
     */
    Integer hotClosingBoxWorkStationIndex_4;
    /**
     * 冷砂合箱工岗标识
     */
    Integer coldClosingBoxWorkStationIndex_1;
    /**
     * 冷砂合箱工岗标识
     */
    Integer coldClosingBoxWorkStationIndex_2;
    /**
     * 冷砂合箱工岗标识
     */
    Integer coldClosingBoxWorkStationIndex_3;
    /**
     * 冷砂合箱工岗标识
     */
    Integer coldClosingBoxWorkStationIndex_4;
    /**
     * 热砂合箱轨道母车在甘特图中的标识
     */
    Integer hotClosingBoxFerryInGanttIndex;
    /**
     * 热砂合箱轨道子车在甘特图中的标识
     */
    Integer hotClosingBoxSubCarInGanttIndex;
    /**
     * 冷砂合箱轨道母车在甘特图中的标识
     */
    Integer coldClosingBoxFerryInGanttIndex;
    /**
     * 冷砂合箱轨道子车在甘特图中的标识
     */
    Integer coldClosingBoxSubCarInGanttIndex;
    /**
     * 热砂合箱工岗1在甘特图中的标识
     */
    Integer hotClosingBoxWorkStationInGanttIndex_1;
    /**
     * 热砂合箱工岗2在甘特图中的标识
     */
    Integer hotClosingBoxWorkStationInGanttIndex_2;
    /**
     * 热砂合箱工岗3在甘特图中的标识
     */
    Integer hotClosingBoxWorkStationInGanttIndex_3;
    /**
     * 热砂合箱工岗4在甘特图中的标识
     */
    Integer hotClosingBoxWorkStationInGanttIndex_4;
    /**
     * 冷砂合箱工岗1在甘特图中的标识
     */
    Integer coldClosingBoxWorkStationInGanttIndex_1;
    /**
     * 冷砂合箱工岗2在甘特图中的标识
     */
    Integer coldClosingBoxWorkStationInGanttIndex_2;
    /**
     * 冷砂合箱工岗3在甘特图中的标识
     */
    Integer coldClosingBoxWorkStationInGanttIndex_3;
    /**
     * 冷砂合箱工岗4在甘特图中的标识
     */
    Integer coldClosingBoxWorkStationInGanttIndex_4;
}
