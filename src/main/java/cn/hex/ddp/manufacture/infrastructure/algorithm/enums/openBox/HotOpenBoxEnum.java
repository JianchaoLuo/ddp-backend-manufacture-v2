package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.openBox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/6 15:26
 */
@AllArgsConstructor
@Getter
public enum HotOpenBoxEnum {
    /**
     * 空载子车前往热砂开箱点位装载砂箱事件
     */
    EMPTY_SUB_CAR_GO_HOT_OPEN_BOX_POSITION_LOAD(0),
    /**
     * 空载子车前往热砂开箱工岗装载砂箱事件
     */
    EMPTY_SUB_CAR_GO_HOT_OPEN_WORK_STATION_LOAD(1),
    /**
     * 空载母车前往热砂开箱工岗事件
     */
    EMPTY_FERRY_GO_HOT_OPEN_WORK_STATION(2),
    /**
     * 空载母车前往热砂开箱点位事件
     */
    EMPTY_FERRY_GO_HOT_OPEN_POSITION(3),
    /**
     * 满载子车前往热砂开箱工岗卸载砂箱的指令
     */
    FULL_SUB_CAR_GO_HOT_OPEN_WORK_STATION_LAND(4),
    /**
     * 满载子车前往热砂砂箱回流点位卸载砂箱的事件
     */
    FULL_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION_LAND(5),
    /**
     * 满载母车前往热砂砂箱回流点位的事件
     */
    FULL_FERRY_GO_HOT_SAND_REFLUX_POSITION(6),

    ;

    private final Integer code;
}
