package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 6:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingPourIndexInput implements Serializable {
    /**
     * 合箱完成前往浇筑轨道母车在甘特图中的标识
     */
    Integer closingBoxGoPourFerryInGanttIndex;
    /**
     * 合箱完成前往浇筑轨道子车在甘特图中的标识
     */
    Integer closingBoxGoPourSubCarInGanttIndex;
}
