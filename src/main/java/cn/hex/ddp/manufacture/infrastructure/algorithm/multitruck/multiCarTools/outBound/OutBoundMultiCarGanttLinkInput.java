package cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.multiCarTools.outBound;

import cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.input.OutBoundSimulationMultiCarInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/11/19 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutBoundMultiCarGanttLinkInput implements Serializable {
    /**
     * 甘特图
     */
    private GanttChart ganttChart;
    /**
     * 区域放置输入
     */
    private OutBoundSimulationMultiCarInput input;
}
