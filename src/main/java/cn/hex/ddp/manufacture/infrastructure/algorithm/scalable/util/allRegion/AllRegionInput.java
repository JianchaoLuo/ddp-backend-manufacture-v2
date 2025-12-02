package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion;

import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.Moulding.MouldingInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.closingBox.ClosingBoxInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.cooling.CoolingInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.moldClosing.MoldClosingInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.openBox.OpenBoxInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.outBound.OutBoundInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.pouring.PouringInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.waitingPour.WaitingPourInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassDescription: 所有区域的仿真输入集合类
 * @Author: KangHong
 * @Created: 2024/11/28 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRegionInput {
    /**
     * 出库区仿真输入
     */
    private OutBoundInput outBoundInput;
    /**
     * 合模区仿真输入
     */
    private MoldClosingInput moldClosingInput;
    /**
     * 造型线仿真输入
     */
    private MouldingInput mouldingInput;
    /**
     * 合箱区仿真输入
     */
    private ClosingBoxInput closingBoxInput;
    /**
     * 待浇筑区仿真输入
     */
    private WaitingPourInput waitingPourInput;
    /**
     * 浇筑区仿真输入
     */
    private PouringInput pouringInput;
    /**
     * 冷却区仿真输入
     */
    private CoolingInput coolingInput;
    /**
     * 开箱区仿真输入
     */
    private OpenBoxInput openBoxInput;
}
