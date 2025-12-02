package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/4 22:18
 */
@AllArgsConstructor
@Getter
public enum ColdSandMoldingEnum {
    /**
     * 空载子车前往冷砂回流点位的事件
     */
    EMPTY_SUB_CAR_GO_COLD_SAND_MOLDING_POSITION_LOAD(0),
    /**
     * 发生子车前往砂箱回流点位搬运回流砂箱的事件
     */
    EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_LAY_POSITION_LOAD(1),
    /**
     * 发生子车空载前往中点等待的事件
     */
    EMPTY_SUB_CAR_GO_COLD_MID_POSITION_WAIT(2),
    /**
     * 发生子车满载前往相应冷砂砂箱合模位置的事件
     */
    FULL_SUB_CAR_GO_COLD_MOLDING_POSITION_LAND(3),
    /**
     * 发生子车前往砂箱回流点位放置砂箱的事件
     */
    FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND(4),
    /**
     * 发生子车前往回流砂箱放置等待点位的事件
     */
    FULL_SUB_CAR_GO_COLD_SAND_WAIT_REFLUX_POSITION(5),
    ;

    private final Integer code;
}
