package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MoldClosingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/23 16:13
 */
@Data
@AllArgsConstructor
public class MoldClosingGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 区域仿真输入
     */
    private MoldClosingSimulationInput input;

    public MoldClosingGanttChartLinkInput() {
    }
}
