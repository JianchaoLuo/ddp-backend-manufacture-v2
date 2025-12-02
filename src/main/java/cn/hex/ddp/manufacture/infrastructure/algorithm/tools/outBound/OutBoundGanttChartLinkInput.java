package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription: 出库区的甘特图和仿真输入的关联类
 * @Author: KangHong
 * @Created: 2024/6/6 19:21
 */
@Data
@AllArgsConstructor
public class OutBoundGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 区域放置输入
     */
    private OutBoundSimulationInput input;

    private boolean testt = false;

    public OutBoundGanttChartLinkInput(GanttChart ganttChart, OutBoundSimulationInput input) {
        this.ganttChart = ganttChart;
        this.input = input;
    }

    public OutBoundGanttChartLinkInput() {
    }
}
