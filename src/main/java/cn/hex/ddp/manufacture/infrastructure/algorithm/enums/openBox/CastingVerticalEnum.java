package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.openBox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/6 15:53
 */
@AllArgsConstructor
@Getter
public enum CastingVerticalEnum {
    /**
     * 前往铸件回流点位，并进行装载
     */
    EMPTY_SUB_CAR_GO_VERTICAL_CASTING_POSITION_LOAD(0),
    /**
     * 前往垂直轨道中点位置
     */
    EMPTY_SUB_CAR_GO_VERTICAL_MID_POSITION_WAIT(1),
    /**
     * 发生前往垂直轨道点位放置铸件的事件
     */
    FULL_SUB_CAR_GO_VERTICAL_CASTING_LAY_POSITION_LAND(2),
    /**
     * 发生满载前往放置铸件的等待点的事件
     */
    FULL_SUB_CAR_GO_VERTICAL_WAIT_CASTING_LAY_POSITION(3),

    ;

    private final Integer code;
}
