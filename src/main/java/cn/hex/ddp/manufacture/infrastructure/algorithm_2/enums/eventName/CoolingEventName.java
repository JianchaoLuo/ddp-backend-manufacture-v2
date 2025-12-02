package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiuYongkang
 * @date 2025/2/24
 * @Description 冷却区事件名称枚举
 */
@Getter
@AllArgsConstructor
public enum CoolingEventName implements EventName {

    /**
     * 浇筑区点位母车满载前往冷却区点位事件
     */
    POURING_POSITION_FERRY_FULL_GO_COOLING_POSITION(0, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区点位母车满载前往冷却区点位完成事件
     */
    POURING_POSITION_FERRY_FULL_GO_COOLING_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车卸载子车事件
     */
    COOLING_POSITION_FERRY_UN_LOAD_SUB_CAR(2, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车卸载子车完成事件
     */
    COOLING_POSITION_FERRY_UN_LOAD_SUB_CAR_FINISHED(3, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位子车满载前往冷却区左暂存点位事件
     */
    COOLING_POSITION_SUB_CAR_FULL_GO_COOLING_LEFT_TEMPORARY_POSITION(4, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位子车满载前往冷却区左暂存点位完成事件
     */
    COOLING_POSITION_SUB_CAR_FULL_GO_COOLING_LEFT_TEMPORARY_POSITION_FINISHED(5, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区子车空载前往冷却区左暂存点位事件
     */
    COOLING_SUB_CAR_EMPTY_GO_COOLING_LEFT_TEMPORARY_POSITION(6, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区子车空载前往冷却区左暂存点位完成事件
     */
    COOLING_SUB_CAR_EMPTY_GO_COOLING_LEFT_TEMPORARY_POSITION_FINISHED(7, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区左暂存点位子车交互事件
     */
    COOLING_LEFT_TEMPORARY_POSITION_SUB_CAR_INTERACTION(8, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区左暂存点位子车交互完成事件
     */
    COOLING_LEFT_TEMPORARY_POSITION_SUB_CAR_INTERACTION_FINISHED(9, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区左暂存点位子车满载前往冷却区事件
     */
    COOLING_POSITION_SUB_CAR_FULL_GO_COOLING(10, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区左暂存点位子车满载前往冷却区完成事件
     */
    COOLING_POSITION_SUB_CAR_FULL_GO_COOLING_FINISHED(11, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却子车卸载浇筑体事件
     */
    COOLING_SUB_CAR_UN_LOAD_CASTING_BODY(12, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却子车卸载浇筑体完成事件
     */
    COOLING_SUB_CAR_UN_LOAD_CASTING_BODY_FINISHED(13, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车装载子车事件
     */
    COOLING_POSITION_FERRY_LOAD_SUB_CAR(14, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车装载子车完成事件
     */
    COOLING_POSITION_FERRY_LOAD_SUB_CAR_FINISHED(15, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车空载前浇筑区点位事件
     */
    COOLING_POSITION_FERRY_EMPTY_GO_POURING_POSITION(16, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车空载前浇筑区点位完成事件
     */
    COOLING_POSITION_FERRY_EMPTY_GO_POURING_POSITION_FINISHED(17, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车前往冷却区下左暂存点位事件
     */
    COOLING_POSITION_FERRY_GO_COOLING_UNDER_LEFT_TEMPORARY_POSITION(18, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车前往冷却区下左暂存点位完成事件
     */
    COOLING_POSITION_FERRY_GO_COOLING_UNDER_LEFT_TEMPORARY_POSITION_FINISHED(19, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车前往上一层左暂存点位事件
     */
    COOLING_POSITION_FERRY_GO_COOLING_TOP_LEFT_TEMPORARY_POSITION(20, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车前往上一层左暂存点位完成事件
     */
    COOLING_POSITION_FERRY_GO_COOLING_TOP_LEFT_TEMPORARY_POSITION_FINISHED(21, NodeEventType.COMPLETE_EVENT);

    private final Integer code;
    private final NodeEventType type;

    @Override
    public void method_x() {
    }
}
