package cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassDescription: 资源集合可生成的事件类
 * @Author: KangHong
 * @Created: 2024/12/7 22:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEvents {
    /**
     * 节点事件Map，key为事件名称，value为事件类型（执行事件/完成事件）
     */
    private List<EventName> events;
}
