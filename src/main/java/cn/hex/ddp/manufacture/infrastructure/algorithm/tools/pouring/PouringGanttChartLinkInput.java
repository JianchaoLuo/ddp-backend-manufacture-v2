package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.PouringSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PouringGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 放置输入
     */
    private PouringSimulationInput input;
}
