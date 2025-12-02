package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.CoolingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoolingGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 放置输入
     */
    private CoolingSimulationInput input;
}
