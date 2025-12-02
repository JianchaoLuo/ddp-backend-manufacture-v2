package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription: 可执行的事件表
 * @Author: KangHong
 * @Created: 2024/11/28 16:25
 */
@Data
@AllArgsConstructor
public class PerformabilityTable {
    /**
     * 出库区可执行事件
     */
    List<ResLinkEvent> outBoundEvents;
    /**
     * 合模区可执行事件
     */
    List<ResLinkEvent> moldClosingEvents;
    /**
     * 造型线可执行事件
     */
    List<ResLinkEvent> mouldingEvents;
    /**
     * 合箱区可执行事件
     */
    List<ResLinkEvent> closingBoxEvents;
    /**
     * 待浇筑区可执行事件
     */
    List<ResLinkEvent> waitingPourEvents;
    /**
     * 浇筑区可执行事件
     */
    List<ResLinkEvent> pouringEvents;
    /**
     * 冷却区可执行事件
     */
    List<ResLinkEvent> coolingEvents;
    /**
     * 开箱区可执行事件
     */
    List<ResLinkEvent> openBoxEvents;
}
