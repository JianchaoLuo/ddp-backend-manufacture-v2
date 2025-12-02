package cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.event;

import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassDescription: 节点事件链接事件类
 * @Author: KangHong
 * @Created: 2024/12/7 22:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeLinkEvent {
    /**
     * 事件名称
     */
    private EventName eventName;
    /**
     * 具体事件
     */
    private Event event;
    /**
     * 事件类型（执行事件/完成事件）
     */
    private NodeEventType eventType;
}
