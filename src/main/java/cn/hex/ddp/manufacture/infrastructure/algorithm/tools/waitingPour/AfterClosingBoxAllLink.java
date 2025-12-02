package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour;

import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.ImitateCoolingPosition;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox.OpenBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringGanttChartLinkInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 6:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfterClosingBoxAllLink implements Serializable {
    /**
     * 合箱区
     */
    private ClosingBoxGanttChartLinkInput closingBoxInput;
    /**
     * 待浇筑区
     */
    private WaitingPourGanttChartLinkInput waitingPourInput;
    /**
     * 浇筑区
     */
    private PouringGanttChartLinkInput pouringInput;
    /**
     * 冷却区
     */
    private CoolingGanttChartLinkInput coolingInput;
    /**
     * 开箱区
     */
    private OpenBoxGanttChartLinkInput openBoxInput;
    /**
     * 出库区
     */
    private OutBoundGanttChartLinkInput outBoundInput;

    /**
     * 待浇筑区的产品计数
     */
    private int waitingPourCount;
    /**
     * 由于冷却区的点位太多，模拟冷却区点位冷却砂箱的时间仿真
     */
    private List<List<ImitateCoolingPosition>> upImitates = new ArrayList<>();
    private List<List<ImitateCoolingPosition>> downImitates = new ArrayList<>();
}
