package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/3 22:19
 */
@AllArgsConstructor
@Getter
public enum HotSandMoldingEnum {
    /**
     * 空载子车前往热砂回流点位的事件
     */
    EMPTY_SUB_CAR_GO_HOT_SAND_MOLDING_POSITION_LOAD(0),
    /**
     * 发生子车前往砂箱回流点位搬运回流砂箱的事件
     */
    EMPTY_SUB_CAR_GO_SAND_BOX_REFLUX_LAY_POSITION_LOAD(1),
    /**
     * 发生子车空载前往中点等待的事件
     */
    EMPTY_SUB_CAR_GO_MID_POSITION_WAIT(2),
    /**
     * 发生子车满载前往相应热砂砂箱合模位置的事件
     */
    FULL_SUB_CAR_GO_HOT_MOLDING_POSITION_LAND(3),
    /**
     * 发生子车前往砂箱回流点位放置砂箱的事件
     */
    FULL_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION_LAND(4),
    /**
     * 发生子车前往回流砂箱放置等待点位的事件
     */
    FULL_SUB_CAR_GO_HOT_SAND_WAIT_REFLUX_POSITION(5),
    /**
     * 发生前往冷砂回流轨道的行车交互点放置冷砂回流砂箱的事件
     */
    FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND(6),
    ;

    private final Integer code;
}
