package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 事件状态枚举
 * @Author: KangHong
 * @Created: 2024/11/9 16:04
 */
@AllArgsConstructor
@Getter
public enum EventState implements Serializable {
    /**
     * 未执行
     */
    UNEXECUTED(0),
    /**
     * 正在请求
     */
    REQUESTING(1),
    /**
     * 正在执行
     */
    EXECUTING(2),
    /**
     * 已完成
     */
    COMPLETED(3),
    ;

    private final Integer code;
}
