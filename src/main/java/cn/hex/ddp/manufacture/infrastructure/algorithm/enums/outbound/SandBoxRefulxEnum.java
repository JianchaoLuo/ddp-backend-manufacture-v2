package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/3 21:53
 */
@AllArgsConstructor
@Getter
public enum SandBoxRefulxEnum {
    /**
     * 空载子车前往冷热砂混合回流放置点位装载回流砂箱的事件
     */
    EMPTY_SUB_CAR_GO_HOT_AND_COLD_REFLUX_POSITION_LOAD(0),
    /**
     * 空载子车前往热砂回流放置点位装载回流砂箱的事件
     */
    EMPTY_SUB_CAR_GO_HOT_REFLUX_POSITION_LOAD(1),
    /**
     * 空载子车前往装载回流等待点的事件
     */
    EMPTY_SUB_CAR_GO_WAIT_POSITION_LOAD(2),
    /**
     * 满载子车前往放置回流砂箱的等待点位的事件
     */
    FULL_SUB_CAR_GO_LAY_WAIT_POSITION_LOAD(3),
    /**
     * 满载子车前往放置回流砂箱点位放置砂箱的事件
     */
    FULL_SUB_CAR_GO_LAY_POSITION_LAND(4);

    private final Integer code;
}
