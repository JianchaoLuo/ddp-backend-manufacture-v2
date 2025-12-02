package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.openBox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/6 15:54
 */
@AllArgsConstructor
@Getter
public enum SandBoxRefluxEnum {
    /**
     * 前往冷热混合砂箱回流点位，并进行装载
     */
    EMPTY_SUB_CAR_GO_COLD_AND_HOT_SAND_REFLUX_POSITION_LOAD(0),
    /**
     * 空载前往砂箱回流轨道的中点进行等待事件
     */
    EMPTY_SUB_CAR_GO_SAND_REFLUX_MID_POSITION_WAIT(1),
    /**
     * 满载前往砂箱回流轨道放置点卸载砂箱的事件
     */
    FULL_SUB_CAR_GO_SAND_REFLUX_LAY_POSITION_LAND(2),
    /**
     * 满载前往砂箱回流轨道放置前等待点的事件
     */
    FULL_SUB_CAR_GO_SAND_REFLUX_WAIT_CASTING_LAY_POSITION(3),

    ;

    private final Integer code;
}
