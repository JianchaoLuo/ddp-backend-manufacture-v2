package cn.hex.ddp.manufacture.infrastructure.algorithm.test;

import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/26 0:00
 */
@Data
@AllArgsConstructor
public class OutBoundANDMoldClosing implements Serializable {
    private OutBoundGanttChartLinkInput outBoundGanttChartLinkInput;
    private MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput;

    private int finishedCount;

    private boolean testt = false;

    public OutBoundANDMoldClosing(OutBoundGanttChartLinkInput outBoundGanttChartLinkInput, MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput,
                                  int finishedCount) {
        this.outBoundGanttChartLinkInput = outBoundGanttChartLinkInput;
        this.moldClosingGanttChartLinkInput = moldClosingGanttChartLinkInput;
        this.finishedCount = finishedCount;
    }
}
