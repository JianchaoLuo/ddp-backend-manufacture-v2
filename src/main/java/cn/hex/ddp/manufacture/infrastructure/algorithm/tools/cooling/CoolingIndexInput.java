package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoolingIndexInput implements Serializable {
    /**
     * 冷却完成前往开箱轨道母车在甘特图中的标识
     */
    Integer coolingGoOpenBoxFerryInGanttIndex;
    /**
     * 冷却完成前往开箱轨道子车在甘特图中的标识
     */
    Integer coolingGoOpenBoxSubCarInGanttIndex;
}
