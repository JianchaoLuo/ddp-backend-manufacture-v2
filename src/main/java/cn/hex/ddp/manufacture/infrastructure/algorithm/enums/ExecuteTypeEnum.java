package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 指令执行者的类型枚举
 * @Author: KangHong
 * @Created: 2024/5/30 20:16
 */
@AllArgsConstructor
@Getter
public enum ExecuteTypeEnum implements Serializable {
    /**
     * 子车
     */
    SUB_CAR(0),
    /**
     * 母车
     */
    FERRY(1),
    /**
     * 设备
     */
    EQUIPMENT(2),
    /**
     * 工岗
     */
    WORK_STATION(3),
    /**
     * 点位
     */
    POSITION(4);

    private final Integer code;
}
