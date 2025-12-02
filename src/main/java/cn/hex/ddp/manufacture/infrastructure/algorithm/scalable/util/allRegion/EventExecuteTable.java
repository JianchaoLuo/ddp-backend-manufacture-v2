package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion;

import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassDescription: 事件执行表
 * @Author: KangHong
 * @Created: 2024/11/28 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventExecuteTable {
    /**
     * 所有正在执行事件的list
     */
    private List<Event> events;
}
