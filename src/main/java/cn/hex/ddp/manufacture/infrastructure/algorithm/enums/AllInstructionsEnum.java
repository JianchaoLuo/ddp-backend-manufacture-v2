package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiuYongkang
 * @date 2025/7/24
 * @Description PLC所有指令的枚举类
 */
@AllArgsConstructor
@Getter
public enum AllInstructionsEnum {
    //车辆-母车

    /**
     * 母车移动指令
     */
    FERRY_RUNNING(0),

    /**
     * 母车装载指令
     */
    FERRY_RISING(1),

    /**
     * 母车卸载指令
     */
    FERRY_LANDING(2),

    /**
     * 母车等待指令
     */
    FERRY_WAITING(3),

    //车辆-子车

    /**
     * 子车移动指令
     */
    SUB_CAR_RUNNING(4),

    /**
     * 子车装载指令
     */
    SUB_CAR_RISING(5),

    /**
     * 子车卸载指令
     */
    SUB_CAR_LANDING(6),

    /**
     * 子车等待指令
     */
    SUB_CAR_WAITING(7),

    //设备

    /**
     * 设备启动指令
     */
    EQUIPMENT_OPEN(8),

    /**
     * 设备关闭指令
     */
    EQUIPMENT_OFF(9),

    //工岗

    /**
     * 工岗加工指令
     */
    WORK_STATION_PROCESSING(10),

    /**
     * 工岗停止加工指令
     */
    WORK_STATION_STOP_PROCESSING(11),
    ;

    private final Integer code;

}
