package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.WaitingPourSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 6:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingPourGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 放置输入
     */
    private WaitingPourSimulationInput input;
}
