package cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.multiCarTools.outBound;

import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/11/8 15:39
 */
@Data
@AllArgsConstructor
public class OutBoundANDMoldClosingMultiCar  implements Serializable {
    /**
     * 出库区
     */
    private OutBoundMultiCarGanttLinkInput outBoundMultiCarGanttLinkInput;
    /**
     * 合模区
     */
    private MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput;

    private int finishedCount;
}
