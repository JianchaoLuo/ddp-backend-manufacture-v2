package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox;


import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/7 22:48
 */
@Data
@AllArgsConstructor
public class ClosingBoxGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 放置输入
     */
    private ClosingBoxSimulationInput input;

    public ClosingBoxGanttChartLinkInput() {
    }
}
