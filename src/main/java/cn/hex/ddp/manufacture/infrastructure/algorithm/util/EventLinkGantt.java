package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 子车,母车,设备,工岗的事件集合类，用于记录子车,母车,设备,工岗的所有事件，并以时间顺序串联记录到list中
 * @Author: KangHong
 * @Created: 2024/5/30 20:56
 */
@AllArgsConstructor
@Data
public class EventLinkGantt implements Serializable {
    /**
     * 子车,母车,设备或工岗id
     */
    Long id;
    /**
     * 执行甘特图的角色类型
     * 子车,母车,设备,工岗
     */
    ExecuteTypeEnum executeType;
    /**
     * 子车或母车的所有事件集合
     */
    List<Event> events;
}
