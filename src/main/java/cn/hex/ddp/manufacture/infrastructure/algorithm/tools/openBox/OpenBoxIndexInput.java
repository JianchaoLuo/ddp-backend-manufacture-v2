package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 0:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenBoxIndexInput implements Serializable {
    /**
     * 冷热混合开箱工岗1
     */
    Integer coldAndHotWorkStation_1;
    /**
     * 冷热混合开箱工岗2
     */
    Integer coldAndHotWorkStation_2;
    /**
     * 冷热混合开箱工岗3
     */
    Integer coldAndHotWorkStation_3;
    /**
     * 冷热混合开箱工岗4
     */
    Integer coldAndHotWorkStation_4;
    /**
     * 冷热混合开箱工岗5
     */
    Integer coldAndHotWorkStation_5;
    /**
     * 冷热混合开箱工岗6
     */
    Integer coldAndHotWorkStation_6;
    /**
     * 冷热混合开箱工岗7
     */
    Integer coldAndHotWorkStation_7;
    /**
     * 冷热混合开箱工岗8
     */
    Integer coldAndHotWorkStation_8;
    /**
     * 热砂开箱工岗1
     */
    Integer hotWorkStation_1;
    /**
     * 热砂开箱工岗2
     */
    Integer hotWorkStation_2;
    /**
     * 热砂开箱工岗3
     */
    Integer hotWorkStation_3;
    /**
     * 热砂开箱工岗4
     */
    Integer hotWorkStation_4;
    /**
     * 热砂开箱工岗5
     */
    Integer hotWorkStation_5;
    /**
     * 热砂开箱工岗6
     */
    Integer hotWorkStation_6;
    /**
     * 热砂开箱工岗7
     */
    Integer hotWorkStation_7;
    /**
     * 热砂开箱工岗8
     */
    Integer hotWorkStation_8;
    /**
     * 冷热混合开箱轨道母车在甘特图中的标识
     */
    Integer coldAndHotFerryInGanttIndex;
    /**
     * 冷热混合开箱轨道子车在甘特图中的标识
     */
    Integer coldAndHotSubCarInGanttIndex;
    /**
     * 热砂开箱轨道母车在甘特图中的标识
     */
    Integer hotFerryInGanttIndex;
    /**
     * 热砂开箱轨道子车在甘特图中的标识
     */
    Integer hotSubCarInGanttIndex;
    /**
     * 铸件回流上轨道子车在甘特图中的标识
     */
    Integer castingUpSubCarInGanttIndex;
    /**
     * 铸件回流下轨道子车在甘特图中的标识
     */
    Integer castingDownSubCarInGanttIndex;
    /**
     * 铸件回流垂直轨道子车在甘特图中的标识
     */
    Integer castingVerticalSubCarInGanttIndex;
    /**
     * 砂箱回流右轨道子车在甘特图中的标识
     */
    Integer sandBoxRefluxRightSubCarInGanttIndex;
    /**
     * 冷热混合开箱工岗1在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_1;
    /**
     * 冷热混合开箱工岗2在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_2;
    /**
     * 冷热混合开箱工岗3在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_3;
    /**
     * 冷热混合开箱工岗4在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_4;
    /**
     * 冷热混合开箱工岗5在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_5;
    /**
     * 冷热混合开箱工岗6在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_6;
    /**
     * 冷热混合开箱工岗7在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_7;
    /**
     * 冷热混合开箱工岗8在甘特图中的标识
     */
    Integer coldAndHotWorkStationInGanttIndex_8;
    /**
     * 热砂开箱工岗1在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_1;
    /**
     * 热砂开箱工岗2在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_2;
    /**
     * 热砂开箱工岗3在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_3;
    /**
     * 热砂开箱工岗4在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_4;
    /**
     * 热砂开箱工岗5在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_5;
    /**
     * 热砂开箱工岗6在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_6;
    /**
     * 热砂开箱工岗7在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_7;
    /**
     * 热砂开箱工岗8在甘特图中的标识
     */
    Integer hotWorkStationInGanttIndex_8;


    Integer positionInGanttIndex;
}
