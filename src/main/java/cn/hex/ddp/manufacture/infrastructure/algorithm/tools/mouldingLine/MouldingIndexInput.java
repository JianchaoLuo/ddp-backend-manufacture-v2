package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/9 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MouldingIndexInput implements Serializable {
    /**
     * 热砂模具和砂芯回流轨道子车在甘特图中的标识
     */
    Integer moldRefluxSubCarInGanttIndex;
    /**
     * 砂箱回流轨道子车在甘特图中的标识
     */
    Integer  hotSpraySandBoxSubCarInGanttIndex;
    /**
     * 冷砂模具回流轨道子车在甘特图中的标识
     */
    Integer coldMoldRefluxSubCarInGanttIndex;
    /**
     * 冷砂搬运轨道子车在甘特图中的标识
     */
    Integer coldSandBoxSubCarInGanttIndex;
    /**
     * 热砂喷涂前往合箱的母车在甘特图中的标识
     */
    Integer hotSprayGoClosingBoxFerryInGanttIndex;
    /**
     * 热砂喷涂前往合箱的子车在甘特图中的标识
     */
    Integer hotSprayGoClosingBoxSubCarInGanttIndex;
    /**
     * 冷砂喷涂前往合箱的母车在甘特图中的标识
     */
    Integer coldSprayGoClosingBoxFerryInGanttIndex;
    /**
     * 冷砂喷涂前往合箱的子车在甘特图中的标识
     */
    Integer coldSprayGoClosingBoxSubCarInGanttIndex;

    //moke造型区
    Integer hotMokeInputInGanttIndex;
    Integer coreMokeInputInGanttIndex;
    Integer coldMokeInputInGanttIndex;
    Integer hotUpMokeInputInGanttIndex;
    Integer hotDownMokeInputInGanttIndex;
    Integer coldUpMokeInputInGanttIndex;
    Integer coldDownMokeInputInGanttIndex;
}
