package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription: 事件类型的枚举，用于表示事件是执行事件/完成事件
 * @Author: KangHong
 * @Created: 2024/12/7 22:19
 */
@Getter
@AllArgsConstructor
public enum NodeEventType {
    /**
     * 执行事件
     */
    EXECUTION_EVENT(0),
    /**
     * 完成事件
     */
    COMPLETE_EVENT(1),
    ;

    private final Integer code;
}
