package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:造型线车辆事件整理
 * @Author: KangHong
 * @Created: 2025/2/21 15:41
 */
@Getter
@AllArgsConstructor
public enum MoldingEventName implements EventName {
    /**
     * 子车空载前往热砂模具回流交互点事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_MOLD_REFLUX_POSITION(0, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往热砂模具回流交互点完成事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_MOLD_REFLUX_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在热砂模具回流交互点位搬运事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_MOLD_REFLUX_POSITION(2, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在热砂模具回流交互点位搬运完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_MOLD_REFLUX_POSITION_FINISHED(3, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往模具回流右交互点位事件
     */
    SUB_CAR_FULL_GO_RIGHT_MOLD_REFLUX_POSITION(4, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载前往模具回流右交互点位完成事件
     */
    SUB_CAR_FULL_GO_RIGHT_MOLD_REFLUX_POSITION_FINISHED(5, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在模具回流右交互点卸载事件
     */
    SUB_CAR_FULL_UNLOAD_AT_RIGHT_MOLD_REFLUX_POSITION(6, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在模具回流右交互点卸载完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_RIGHT_MOLD_REFLUX_POSITION_FINISHED(7, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往砂芯模具回流交互点位事件
     */
    SUB_CAR_EMPTY_GO_CORE_MOLD_REFLUX_POSITION(8, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往砂芯模具回流交互点位完成事件
     */
    SUB_CAR_EMPTY_GO_CORE_MOLD_REFLUX_POSITION_FINISHED(9, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在砂芯回流交互点位搬运事件
     */
    SUB_CAR_EMPTY_LOAD_AT_CORE_MOLD_REFLUX_POSITION(10, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在砂芯回流交互点位搬运完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_CORE_MOLD_REFLUX_POSITION_FINISHED(11, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往冷砂模具回流交互点位事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_MOLD_REFLUX_POSITION(12, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往冷砂模具回流交互点位完成事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_MOLD_REFLUX_POSITION_FINISHED(13, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在冷砂模具回流交互点位搬运事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_MOLD_REFLUX_POSITION(14, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在冷砂模具回流交互点位搬运完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_MOLD_REFLUX_POSITION_FINISHED(15, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往冷砂喷涂交互点位事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_SPRAY_POSITION(16, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往冷砂喷涂交互点位完成事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_SPRAY_POSITION_FINISHED(17, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在冷砂喷涂交互点位搬运事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_SPRAY_POSITION(18, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在冷砂喷涂交互点位搬运完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_SPRAY_POSITION_FINISHED(19, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往模具回流轨道右交互点位事件
     */
    SUB_CAR_FULL_GO_MOLD_REFLUX_RIGHT_POSITION(20, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载前往模具回流轨道右交互点位完成事件
     */
    SUB_CAR_FULL_GO_MOLD_REFLUX_RIGHT_POSITION_FINISHED(21, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在模具回流轨道右交互点位卸载事件
     */
    SUB_CAR_FULL_UNLOAD_AT_MOLD_REFLUX_RIGHT_POSITION(22, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在模具回流轨道右交互点位卸载完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_MOLD_REFLUX_RIGHT_POSITION_FINISHED(23, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往冷砂喷涂机事件
     */
    SUB_CAR_FULL_GO_COLD_SAND_SPRAY(24, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载前往冷砂喷涂机完成事件
     */
    SUB_CAR_FULL_GO_COLD_SAND_SPRAY_FINISHED(25, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂喷涂机卸载物品事件
     */
    SUB_CAR_FULL_UNLOAD_AT_COLD_SAND_SPRAY(26, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在冷砂喷涂机卸载物品完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_COLD_SAND_SPRAY_FINISHED(27, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车空载前往热砂喷涂机事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_SPRAY(28, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往热砂喷涂机完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_SPRAY_FINISHED(29, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往热砂喷涂机事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_SPRAY(30, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往热砂喷涂机完成事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_SPRAY_FINISHED(31, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在热砂喷涂机搬运物品事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_SPRAY(32, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在热砂喷涂机搬运物品完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_SPRAY_FINISHED(33, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂喷涂机返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_HOT_SAND_SPRAY(34, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂喷涂机返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_HOT_SAND_SPRAY_FINISHED(35, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载前往热砂合箱区交互点事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_POSITION(36, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车满载前往热砂合箱区交互点完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(37, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往热砂合箱区交互点事件
     */
    SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_POSITION(38, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载前往热砂合箱区交互点完成事件
     */
    SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(39, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂合箱区交互点卸载物品事件
     */
    SUB_CAR_FULL_UNLOAD_AT_HOT_SAND_CLOSING_BOX_POSITION(40, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂合箱区交互点卸载物品完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(41, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂合箱区交互点返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_POSITION(42, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂合箱区交互点返回母车完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(43, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车空载前往冷砂喷涂机事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_COLD_SAND_SPRAY(44, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往冷砂喷涂机完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_COLD_SAND_SPRAY_FINISHED(45, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往冷砂喷涂机事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_SPRAY(46, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往冷砂喷涂机完成事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_SPRAY_FINISHED(47, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在喷涂机搬运物品事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_SPRAY(48, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在喷涂机搬运物品完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_SPRAY_FINISHED(49, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在喷涂机返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_COLD_SAND_SPRAY(50, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在喷涂机返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_COLD_SAND_SPRAY_FINISHED(51, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载前往冷砂合箱区交互点事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_POSITION(52, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车满载前往冷砂合箱区交互点完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(53, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往冷砂合箱区交互点事件
     */
    SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_POSITION(54, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载前往冷砂合箱区交互点完成事件
     */
    SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(55, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂合箱区交互点卸载物品事件
     */
    SUB_CAR_FULL_UNLOAD_AT_COLD_SAND_CLOSING_BOX_POSITION(56, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在冷砂合箱区交互点卸载物品完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(57, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂合箱区交互点返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_POSITION(58, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在冷砂合箱区交互点返回母车完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(59, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在模具回流垂直轨道返回默认位置事件
     */
    SUB_CAR_EMPTY_ON_MOLD_REFLUX_VERTICAL_PATH_BACK_DEFAULT_LOCATION(60, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在模具回流垂直轨道返回默认位置完成事件
     */
    SUB_CAR_EMPTY_ON_MOLD_REFLUX_VERTICAL_PATH_BACK_DEFAULT_LOCATION_FINISHED(61, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在冷砂喷涂垂直轨道返回默认位置事件
     */
    SUB_CAR_EMPTY_ON_COLD_SAND_SPRAY_VERTICAL_PATH_BACK_DEFAULT_LOCATION(62, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在冷砂喷涂垂直轨道返回默认位置完成事件
     */
    SUB_CAR_EMPTY_ON_COLD_SAND_SPRAY_VERTICAL_PATH_BACK_DEFAULT_LOCATION_FINISHED(63, NodeEventType.COMPLETE_EVENT),
    ;

    private final Integer code;

    private final NodeEventType type;

    @Override
    public void method_x() {
    }
}
