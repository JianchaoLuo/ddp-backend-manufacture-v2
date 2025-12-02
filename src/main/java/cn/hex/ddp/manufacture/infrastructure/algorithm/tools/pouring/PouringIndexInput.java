package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PouringIndexInput implements Serializable {
    /**
     * 浇筑区工岗标识
     */
    Integer pouringWorkStationIndex_1;
    Integer pouringWorkStationIndex_2;
    Integer pouringWorkStationIndex_3;
    Integer pouringWorkStationIndex_4;
    Integer pouringWorkStationIndex_5;
    Integer pouringWorkStationIndex_6;
    Integer pouringWorkStationIndex_7;
    Integer pouringWorkStationIndex_8;
    Integer pouringWorkStationIndex_9;
    Integer pouringWorkStationIndex_10;
    Integer pouringWorkStationIndex_11;
    Integer pouringWorkStationIndex_12;
    /**
     * 浇筑轨道母车在甘特图中的标识
     */
    Integer pouringFerryInGanttIndex;
    /**
     * 浇筑轨道子车在甘特图中的标识
     */
    Integer pouringSubCarInGanttIndex;
    /**
     * 浇筑区工岗在甘特图中的标识
     */
    Integer pouringWorkStationInGanttIndex_1;
    Integer pouringWorkStationInGanttIndex_2;
    Integer pouringWorkStationInGanttIndex_3;
    Integer pouringWorkStationInGanttIndex_4;
    Integer pouringWorkStationInGanttIndex_5;
    Integer pouringWorkStationInGanttIndex_6;
    Integer pouringWorkStationInGanttIndex_7;
    Integer pouringWorkStationInGanttIndex_8;
    Integer pouringWorkStationInGanttIndex_9;
    Integer pouringWorkStationInGanttIndex_10;
    Integer pouringWorkStationInGanttIndex_11;
    Integer pouringWorkStationInGanttIndex_12;
}
