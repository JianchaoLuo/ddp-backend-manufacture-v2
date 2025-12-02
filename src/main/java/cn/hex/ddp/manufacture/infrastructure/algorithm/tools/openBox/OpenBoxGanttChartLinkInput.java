package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 0:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenBoxGanttChartLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 放置输入
     */
    private OpenBoxSimulationInput input;
}
