package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/28 20:02
 */
@AllArgsConstructor
@Getter
public enum OutBoundCarEventEnum {
    /**
     * 空载子车前往热砂回流点位的事件
     */
    EMPTY_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION(0),
    /**
     * 空载母车前往热砂回流点位的事件
     */
    EMPTY_FERRY_GO_HOT_SAND_REFLUX_POSITION(1),
    /**
     * 空载子车前往模具回流点位的事件
     */
    EMPTY_SUB_CAR_GO_MOLD_REFLUX_POSITION(2),
    /**
     * 空载母车前往模具回流点位的事件
     */
    EMPTY_FERRY_GO_MOLD_REFLUX_POSITION(3),
    /**
     * 空载子车前往冷砂回流点位的事件
     */
    EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION(4),
    /**
     * 空载母车前往冷砂回流点位的事件
     */
    EMPTY_FERRY_GO_COLD_SAND_REFLUX_POSITION(5),
    /**
     * 子车在出库点1进行出库
     */
    EMPTY_SUB_CAR_OUT_BOUND_IN_OUT_POSITION_1(6),
    /**
     * 母车在出库点1进行出库
     */
    EMPTY_FERRY_OUT_BOUND_GO_OUT_POSITION_1(7),
    /**
     * 子车在出库点2进行出库
     */
    EMPTY_SUB_CAR_OUT_BOUND_IN_OUT_POSITION_2(8),
    /**
     * 母车在出库点2进行出库
     */
    EMPTY_FERRY_OUT_BOUND_GO_OUT_POSITION_2(9),
    /**
     * 空载子车在热砂回流点位装载的事件
     */
    EMPTY_SUB_CAR_IN_HOT_SAND_REFLUX_POSITION_LOAD(10),
    /**
     * 空载子车在模具回流点位装载的事件
     */
    EMPTY_SUB_CAR_IN_MOLD_REFLUX_POSITION_LOAD(11),
    /**
     * 空载子车在冷砂回流点位装载的事件
     */
    EMPTY_SUB_CAR_IN_COLD_SAND_REFLUX_POSITION_LOAD(12),
    /**
     * 空载子车在出库点位1装载的事件
     */
    EMPTY_SUB_CAR_IN_OUT_BOUND_POSITION_LOAD_1(13),
    /**
     * 空载子车在出库点位2装载的事件
     */
    EMPTY_SUB_CAR_IN_OUT_BOUND_POSITION_LOAD_2(14),
    /**
     * 空载子车在出库点位1返回母车的事件
     */
    EMPTY_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION_1(15),
    /**
     * 空载子车在出库点位2返回母车的事件
     */
    EMPTY_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION_2(16),
    /**
     * 空载子车在砂箱回流点位返回母车的事件
     */
    EMPTY_SUB_CAR_BACK_FERRY_IN_SAND_REFLUX_POSITION(17),
    /**
     * 空载子车在模具回流点位返回母车的事件
     */
    EMPTY_SUB_CAR_BACK_FERRY_IN_MOLD_REFLUX_POSITION(18),
    /**
     * 满载子车前往热砂合模交互点的事件
     */
    FULL_SUB_CAR_GO_HOT_MOLDING_POSITION(19),
    /**
     * 满载母车前往热砂合模交互点的事件
     */
    FULL_FERRY_GO_HOT_MOLDING_POSITION(20),
    /**
     * 满载子车前往冷砂合模交互点的事件
     */
    FULL_SUB_CAR_GO_COLD_MOLDING_POSITION(21),
    /**
     * 满载母车前往冷砂合模交互点的事件
     */
    FULL_FERRY_GO_COLD_MOLDING_POSITION(22),
    /**
     * 满载子车前往制芯交互点的事件
     */
    FULL_SUB_CAR_GO_CORE_MAKING_POSITION(23),
    /**
     * 满载母车前往制芯交互点的事件
     */
    FULL_FERRY_GO_CORE_MAKING_POSITION(24),
    /**
     * 满载子车前往出库点1卸载的事件
     */
    FULL_SUB_CAR_GO_OUT_BOUND_POSITION_LAND(25),
    /**
     * 满载母车前往出库点卸载回流产品的事件
     */
    FULL_FERRY_GO_OUT_BOUND_POSITION_LAND(26),
    /**
     * 满载子车返回母车的事件
     */
    FULL_SUB_CAR_GO_BACK_FERRY(27),
    /**
     * 满载子车在出库点卸载产品的事件
     */
    FULL_SUB_CAR_IN_OUT_BOUND_POSITION_LAND(28),
    /**
     * 满载子车在出库点装载产品后返回母车的事件
     */
    FULL_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION(29),
    /**
     * 满载子车在热砂合模交互点位卸载模具的事件
     */
    FULL_SUB_CAR_LAND_IN_HOT_MOLDING_POSITION(30),
    /**
     * 满载子车在冷砂合模交互点位卸载模具的事件
     */
    FULL_SUB_CAR_LAND_IN_COLD_MOLDING_POSITION(31),
    /**
     * 满载子车在制芯交互点位卸载模具的事件
     */
    FULL_SUB_CAR_LAND_IN_CORE_MAKING_POSITION(32),
    /**
     * 满载母车前往热砂砂箱合模点位的事件
     */
    FULL_FERRY_GO_HOT_SAND_MOLDING_POSITION(33),
    /**
     * 满载子车前往热砂砂箱合模热砂放置点位放置砂箱的事件
     */
    FULL_SUB_CAR_GO_HOT_SAND_MOLDING_POSITION_LAND(34),
    /**
     * 满载母车前往冷砂砂箱合模点位的事件
     */
    FULL_FERRY_GO_COLD_SAND_MOLDING_POSITION(35),
    /**
     * 满载子车前往冷砂砂箱合模冷砂放置点位放置砂箱的事件
     */
    FULL_SUB_CAR_GO_COLD_SAND_MOLDING_POSITION_LAND(36),

    ;

    private final Integer code;
}
