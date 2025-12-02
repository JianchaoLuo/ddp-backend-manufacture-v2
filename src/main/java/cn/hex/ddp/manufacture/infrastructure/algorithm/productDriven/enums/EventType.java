package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 事件类型枚举
 * @Author: KangHong
 * @Created: 2024/11/9 16:19
 */
@AllArgsConstructor
@Getter
public enum EventType implements Serializable {
    /**
     * 独立事件
     */
    INDEPENDENT(0),
    /**
     * 合作事件
     */
    COOPERATION(1),
    ;

    private final Integer code;
}
