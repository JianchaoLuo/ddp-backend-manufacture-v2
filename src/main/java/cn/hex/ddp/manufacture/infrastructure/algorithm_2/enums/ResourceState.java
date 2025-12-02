package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription: 资源状态的枚举，用于表示资源处于空闲/忙碌状态
 * @Author: KangHong
 * @Created: 2024/12/7 22:02
 */
@Getter
@AllArgsConstructor
public enum ResourceState {
    /**
     * 资源空闲，资源可以接收指令
     */
    IDLE(0),
    /**
     * 资源忙碌，资源正在执行指令
     */
    BUSY(1),
    ;

    private final Integer code;
}
