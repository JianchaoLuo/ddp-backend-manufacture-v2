package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:合箱区事件枚举
 * @Author: KangHong
 * @Created: 2025/2/21 15:41
 */
@Getter
@AllArgsConstructor
public enum ClosingBoxEventName implements EventName {
    /**
     * 子母车空载前往热砂合箱交互点事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_POSITION(0, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往热砂合箱交互点完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往热砂合箱交互点事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_POSITION(2, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往热砂合箱交互点完成事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(3, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在热砂合箱交互点搬运砂箱事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_CLOSING_BOX_POSITION(4, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在热砂合箱交互点搬运砂箱完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(5, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂合箱交互点返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_POSITION(6, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂合箱交互点返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_POSITION_FINISHED(7, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载在热砂合箱轨道前往合箱工岗事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION(8, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车满载在热砂合箱轨道前往合箱工岗完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(9, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂合箱轨道前往合箱工岗事件
     */
    SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION(10, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂合箱轨道前往合箱工岗完成事件
     */
    SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(11, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂合箱工岗卸载砂箱事件
     */
    SUB_CAR_FULL_UNLOAD_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION(12, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂合箱工岗卸载砂箱完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(13, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在热砂合箱工岗返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION(14, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在热砂合箱工岗返回母车完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(15, NodeEventType.COMPLETE_EVENT),
    /**
     * 合箱工岗进行合箱事件
     */
    CLOSING_BOX_WORK_POSITION_WORK(16, NodeEventType.EXECUTION_EVENT),
    /**
     * 合箱工岗进行合箱完成事件
     */
    CLOSING_BOX_WORK_POSITION_WORK_FINISHED(17, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车空载前往热砂合箱工岗事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION(18, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往热砂合箱工岗完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(19, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往热砂合箱工岗事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION(20, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往热砂合箱工岗完成事件
     */
    SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(21, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在热砂合箱工岗搬运砂箱事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION(22, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在热砂合箱工岗搬运砂箱完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(23, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在热砂合箱工岗返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION(24, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在热砂合箱工岗返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_HOT_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(25, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载在热砂合箱轨道前往待浇筑区上交互点事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_WAIT_POURING_UP_POSITION(26, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车满载在热砂合箱轨道前往待浇筑区上交互点完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_WAIT_POURING_UP_POSITION_FINISHED(27, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往待浇筑区上交互点事件
     */
    SUB_CAR_FULL_GO_WAIT_POURING_UP_POSITION(28, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载前往待浇筑区上交互点完成事件
     */
    SUB_CAR_FULL_GO_WAIT_POURING_UP_POSITION_FINISHED(29, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在待浇筑区上交互点卸载砂箱事件
     */
    SUB_CAR_FULL_UNLOAD_AT_WAIT_POURING_UP_POSITION(30, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在待浇筑区上交互点卸载砂箱完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_WAIT_POURING_UP_POSITION_FINISHED(31, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在在待浇筑区上交互点返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_WAIT_POURING_UP_POSITION(32, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在在待浇筑区上交互点返回母车完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_WAIT_POURING_UP_POSITION_FINISHED(33, NodeEventType.COMPLETE_EVENT),
    /**
     * 子子母车空载前往冷砂合箱交互点事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_POSITION(34, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往冷砂合箱交互点完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(35, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往冷砂合箱交互点事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_POSITION(36, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往冷砂合箱交互点完成事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(37, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在冷砂合箱交互点搬运砂箱事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_CLOSING_BOX_POSITION(38, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在冷砂合箱交互点搬运砂箱完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(39, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂合箱交互点返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_POSITION(40, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在冷砂合箱交互点返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_POSITION_FINISHED(41, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载在冷砂合箱轨道前往合箱工岗事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION(42, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车满载在冷砂合箱轨道前往合箱工岗完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(43, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂合箱轨道前往合箱工岗事件
     */
    SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION(44, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在冷砂合箱轨道前往合箱工岗完成事件
     */
    SUB_CAR_FULL_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(45, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂合箱工岗卸载砂箱事件
     */
    SUB_CAR_FULL_UNLOAD_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION(46, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在冷砂合箱工岗卸载砂箱完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(47, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在冷砂合箱工岗返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION(48, NodeEventType.EXECUTION_EVENT),
    /**
     *子车空载在冷砂合箱工岗返回母车事件完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(49, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车空载前往冷砂合箱工岗事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION(50, NodeEventType.EXECUTION_EVENT),
    /**
     *子母车空载前往冷砂合箱工岗完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(51, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往冷砂合箱工岗事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION(52, NodeEventType.EXECUTION_EVENT),
    /**
     *子车空载前往冷砂合箱工岗完成事件
     */
    SUB_CAR_EMPTY_GO_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(53, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在冷砂合箱工岗搬运砂箱事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION(54, NodeEventType.EXECUTION_EVENT),
    /**
     *子车空载在冷砂合箱工岗搬运砂箱完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(55, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在冷砂合箱工岗返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION(56, NodeEventType.EXECUTION_EVENT),
    /**
     *子车满载在冷砂合箱工岗返回母车事件完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_COLD_SAND_CLOSING_BOX_WORK_POSITION_FINISHED(57, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载在冷砂合箱轨道前往待浇筑区下交互点事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_WAIT_POURING_DOWN_POSITION(58, NodeEventType.EXECUTION_EVENT),
    /**
     *子母车满载在冷砂合箱轨道前往待浇筑区下交互点完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_WAIT_POURING_DOWN_POSITION_FINISHED(59, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载前往待浇筑区下交互点前往合箱工岗事件
     */
    SUB_CAR_FULL_GO_WAIT_POURING_DOWN_POSITION(60, NodeEventType.EXECUTION_EVENT),
    /**
     *子车满载前往待浇筑区下交互点前往合箱工岗完成事件
     */
    SUB_CAR_FULL_GO_WAIT_POURING_DOWN_POSITION_FINISHED(61, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在待浇筑区下交互点卸载砂箱事件
     */
    SUB_CAR_FULL_UNLOAD_AT_WAIT_POURING_DOWN_POSITION(62, NodeEventType.EXECUTION_EVENT),
    /**
     *子车满载在待浇筑区下交互点卸载砂箱完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_WAIT_POURING_DOWN_POSITION_FINISHED(63, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在在待浇筑区下交互点返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_WAIT_POURING_DOWN_POSITION(64, NodeEventType.EXECUTION_EVENT),
    /**
     *子车空载在在待浇筑区下交互点返回母车事件完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_WAIT_POURING_DOWN_POSITION_FINISHED(65, NodeEventType.COMPLETE_EVENT),
    ;

    private final Integer code;

    private final NodeEventType type;

    @Override
    public void method_x() {
    }
}
