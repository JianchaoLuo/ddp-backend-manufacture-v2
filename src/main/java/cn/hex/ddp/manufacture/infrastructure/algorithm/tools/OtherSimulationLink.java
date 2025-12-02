package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;

import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OutBoundANDMoldClosing;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.MouldingGanttChartLinkInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/17 20:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherSimulationLink implements Serializable {
    OutBoundANDMoldClosing outBoundANDMoldClosing;
    MouldingGanttChartLinkInput mouldingGanttChartLinkInput;
    ClosingBoxGanttChartLinkInput closingBoxGanttChartLinkInput;
}
