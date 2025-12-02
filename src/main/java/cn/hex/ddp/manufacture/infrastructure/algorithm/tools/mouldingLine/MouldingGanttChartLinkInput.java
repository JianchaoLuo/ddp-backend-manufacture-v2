package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MouldingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/7 22:34
 */
@Data
@AllArgsConstructor
public class MouldingGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 区域仿真输入
     */
    private MouldingSimulationInput input;

    public MouldingGanttChartLinkInput() {
    }
}
