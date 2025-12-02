package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 指令的类型枚举
 * @Author: KangHong
 * @Created: 2024/5/30 20:07
 */
@AllArgsConstructor
@Getter
public enum InstructionsEnum implements Serializable {
    /**
     * 车辆移动指令
     */
    RUNNING(0),
    /**
     * 车辆顶杆升指令
     */
    RISING(1),
    /**
     * 车辆顶杆降指令
     */
    LANDING(2),
    /**
     * 设备开指令
     */
    OPEN(3),
    /**
     * 设备关指令
     */
    OFF(4),
    /**
     * 通知设备或工岗或点位产品到达指令
     */
    NOTICE(5),
    /**
     * 准备铁水
     */
    PREPARE_MOLTEN_IRON(6),
    /**
     * 使用铁水
     */
    USE_MOLTEN_IRON(7),

    /**
     * 车辆等待指令
     */
    CAR_WAITING(8),
    ;

    private final Integer code;
}
