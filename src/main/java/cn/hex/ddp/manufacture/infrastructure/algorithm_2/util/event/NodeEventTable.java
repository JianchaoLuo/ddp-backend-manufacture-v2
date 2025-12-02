package cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.event;

import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassDescription: 所有事件存储列表
 * @Author: KangHong
 * @Created: 2024/12/7 22:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeEventTable {
    /**
     * 事件列表
     */
    private List<NodeLinkEvent> nodeLinkEvents;

    /**
     * 通过事件名称枚举获取相应事件
     * @param eventName 事件名称枚举
     * @return 相应的事件
     */
    public Event getEventByEventName(EventName eventName) {
        for (NodeLinkEvent nodeLinkEvent : nodeLinkEvents) {
            if (nodeLinkEvent.getEvent().getEventName().equals(eventName.toString())) {
                return nodeLinkEvent.getEvent();
            }
        }
        return null; // 如果没有找到匹配的事件，返回 null
    }
}
