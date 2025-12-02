package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.openBox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/6 15:52
 */
@AllArgsConstructor
@Getter
public enum CastingUpEnum {
    /**
     * 前往上铸件托盘回流点位，并进行装载
     */
    EMPTY_SUB_CAR_GO_UP_TRAY_LAY_POSITION_LOAD(0),
    /**
     * 前往上轨道中点位置
     */
    EMPTY_SUB_CAR_GO_UP_MID_POSITION_WAIT(1),
    /**
     * 发生前往上垂直轨道点位放置铸件的事件
     */
    FULL_SUB_CAR_GO_UP_CASTING_LAY_POSITION_LAND(2),
    /**
     * 发生满载前往上放置铸件的等待点的事件
     */
    FULL_SUB_CAR_GO_UP_WAIT_CASTING_LAY_POSITION(3),
    /**
     * 半满载前往开箱工岗装载零件
     */
    HALF_SUB_CAR_GO_WORK_STATION_LOAD_CASTING(4),

    ;

    private final Integer code;
}
