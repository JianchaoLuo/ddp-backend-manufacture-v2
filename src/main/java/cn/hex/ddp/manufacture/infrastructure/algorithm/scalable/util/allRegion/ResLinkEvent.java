package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription: 链接事件与对应资源的中间类
 * @Author: KangHong
 * @Created: 2024/11/29 15:44
 */
@Data
@AllArgsConstructor
public class ResLinkEvent {
    /**
     * 资源的可执行事件列表
     */
    List<Event> events;
    /**
     * 子车id，母车id，设备id，工岗id，道路id
     */
    Long id;
    /**
     * 指令执行者类型
     */
    ExecuteTypeEnum executeType;
}
