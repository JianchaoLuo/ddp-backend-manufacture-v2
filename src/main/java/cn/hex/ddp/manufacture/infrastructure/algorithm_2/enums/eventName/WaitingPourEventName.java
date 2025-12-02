package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:待浇筑区事件枚举
 * @Author: KangHong
 * @Created: 2025/2/21 15:41
 */
@Getter
@AllArgsConstructor
public enum WaitingPourEventName implements EventName {
    /**
     * 子母车空载前往待浇筑区上交互点事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_WAIT_POURING_UP_POSITION(0, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往待浇筑区上交互点完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_WAIT_POURING_UP_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往待浇筑区上交互点事件
     */
    SUB_CAR_EMPTY_GO_WAIT_POURING_UP_POSITION(2, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往待浇筑区上交互点完成事件
     */
    SUB_CAR_EMPTY_GO_WAIT_POURING_UP_POSITION_FINISHED(3, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在待浇筑区上交互点搬运砂箱事件
     */
    SUB_CAR_EMPTY_LOAD_AT_WAIT_POURING_UP_POSITION(4, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在待浇筑区上交互点搬运砂箱完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_WAIT_POURING_UP_POSITION_FINISHED(5, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在待浇筑区上交互点返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_WAIT_POURING_UP_POSITION(6, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在待浇筑区上交互点返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_WAIT_POURING_UP_POSITION_FINISHED(7, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车满载在待浇筑轨道前往暂存点事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_POURING_STORE_POSITION(8, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车满载在待浇筑轨道前往暂存点完成事件
     */
    FERRY_AND_SUB_CAR_FULL_GO_POURING_STORE_POSITION_FINISHED(9, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在待浇筑轨道前往暂存点事件
     */
    SUB_CAR_FULL_GO_POURING_STORE_POSITION(10, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在待浇筑轨道前往暂存点完成事件
     */
    SUB_CAR_FULL_GO_POURING_STORE_POSITION_FINISHED(11, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在待浇筑暂存点卸载砂箱组事件
     */
    SUB_CAR_FULL_UNLOAD_AT_POURING_STORE_POSITION(12, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在待浇筑暂存点卸载砂箱组完成事件
     */
    SUB_CAR_FULL_UNLOAD_AT_POURING_STORE_POSITION_FINISHED(13, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在待浇筑暂存点返回母车事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_POURING_STORE_POSITION(14, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在待浇筑暂存点返回母车完成事件
     */
    SUB_CAR_EMPTY_BACK_FERRY_AT_POURING_STORE_POSITION_FINISHED(15, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在待浇筑轨道前往暂存点事件
     */
    SUB_CAR_EMPTY_GO_POURING_STORE_POSITION(16, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在待浇筑轨道前往暂存点完成事件
     */
    SUB_CAR_EMPTY_GO_POURING_STORE_POSITION_FINISHED(17, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在待浇筑暂存点装载砂箱组事件
     */
    SUB_CAR_EMPTY_LOAD_AT_POURING_STORE_POSITION(18, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在待浇筑暂存点装载砂箱组完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_POURING_STORE_POSITION_FINISHED(19, NodeEventType.COMPLETE_EVENT),
    /**
     * 子母车空载前往待浇筑区下交互点事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_WAIT_POURING_DOWN_POSITION(20, NodeEventType.EXECUTION_EVENT),
    /**
     * 子母车空载前往待浇筑区下交互点完成事件
     */
    FERRY_AND_SUB_CAR_EMPTY_GO_WAIT_POURING_DOWN_POSITION_FINISHED(21, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载前往待浇筑区下交互点事件
     */
    SUB_CAR_EMPTY_GO_WAIT_POURING_DOWN_POSITION(22, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载前往待浇筑区下交互点完成事件
     */
    SUB_CAR_EMPTY_GO_WAIT_POURING_DOWN_POSITION_FINISHED(23, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在待浇筑区下交互点搬运砂箱事件
     */
    SUB_CAR_EMPTY_LOAD_AT_WAIT_POURING_DOWN_POSITION(24, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在待浇筑区下交互点搬运砂箱完成事件
     */
    SUB_CAR_EMPTY_LOAD_AT_WAIT_POURING_DOWN_POSITION_FINISHED(25, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车满载在待浇筑区下交互点返回母车事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_WAIT_POURING_DOWN_POSITION(26, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车满载在待浇筑区下交互点返回母车完成事件
     */
    SUB_CAR_FULL_BACK_FERRY_AT_WAIT_POURING_DOWN_POSITION_FINISHED(27, NodeEventType.COMPLETE_EVENT),
    /**
     * 子车空载在待浇筑轨道回到默认位置事件
     */
    SUB_CAR_EMPTY_ON_WAIT_POURING_PATH_BACK_DEFAULT_LOCATION(28, NodeEventType.EXECUTION_EVENT),
    /**
     * 子车空载在待浇筑轨道回到默认位置完成事件
     */
    SUB_CAR_EMPTY_ON_WAIT_POURING_PATH_BACK_DEFAULT_LOCATION_FINISHED(29, NodeEventType.COMPLETE_EVENT),
    ;

    private final Integer code;

    private final NodeEventType type;

    @Override
    public void method_x() {
    }
}
