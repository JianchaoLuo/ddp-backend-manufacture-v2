package cn.hex.ddp.manufacture.infrastructure.algorithm.data;

import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class SimulateResult {
    /**
     * 最终的事件甘特图
     */
    private List<GanttChart> ganttCharts;
    /**
     * 订单时间的Map
     */
    Map<Long, Double> orderTimes;
}
