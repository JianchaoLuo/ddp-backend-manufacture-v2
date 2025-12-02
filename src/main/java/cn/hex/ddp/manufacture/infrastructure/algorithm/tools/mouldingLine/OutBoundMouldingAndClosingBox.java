package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/7 22:33
 */
@Data
@AllArgsConstructor
public class OutBoundMouldingAndClosingBox implements Serializable {
    /**
     * 出库区
     */
    private OutBoundGanttChartLinkInput outBoundInput;
    /**
     * 造型线
     */
    private MouldingGanttChartLinkInput mouldingInput;
    /**
     * 合箱区
     */
    private ClosingBoxGanttChartLinkInput closingBoxInput;

    public OutBoundMouldingAndClosingBox() {
    }


}
