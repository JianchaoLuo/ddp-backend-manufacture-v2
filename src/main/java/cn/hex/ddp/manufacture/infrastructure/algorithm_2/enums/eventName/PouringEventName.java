package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author LiuYongkang
 * @date 2025/2/24
 * @Description 浇筑区事件名称枚举
 */
@Getter
@AllArgsConstructor
public enum PouringEventName implements EventName {

    /**
     * 浇筑区轨道上子母车空载前往浇筑区轨道上方点位事件
     */
    POURING_TOP_FERRY_EMPTY_GO_POURING_TOP_POSITION(0, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区轨道上子母车空载前往浇筑区轨道上方点位完成事件
     */
    POURING_TOP_FERRY_EMPTY_GO_POURING_TOP_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区轨道母车空载前往待浇筑区点位事件
     */
    POURING_FERRY_EMPTY_GO_POSITION(2, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区轨道母车空载前往待浇筑区点位完成事件
     */
    POURING_FERRY_EMPTY_GO_POSITION_FINISHED(3, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区轨道点位母车卸载子车事件
     */
    POURING_POSITION_FERRY_UN_LOAD(4, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区轨道点位母车卸载子车完成事件
     */
    POURING_POSITION_FERRY_UN_LOAD_FINISHED(5, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区轨道点位子车空载前往待浇筑区暂存点位事件
     */
    POURING_POSITION_SUB_CAR_GO_WAITING_POURING_TEMPORARY_POSITION(6, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区轨道点位子车空载前往待浇筑区暂存点位完成事件
     */
    POURING_POSITION_SUB_CAR_GO_WAITING_POURING_TEMPORARY_POSITION_FINISHED(7, NodeEventType.COMPLETE_EVENT),

    /**
     * 待浇筑区子车空载前往待浇筑区点位事件
     */
    WAITING_POURING_SUB_CAR_EMPTY_GO_WAITING_POURING(8, NodeEventType.EXECUTION_EVENT),

    /**
     * 待浇筑区子车空载前往待浇筑区点位完成事件
     */
    WAITING_POURING_SUB_CAR_EMPTY_GO_WAITING_POURING_FINISHED(9, NodeEventType.COMPLETE_EVENT),

    /**
     * 待浇筑区子车满载前往待浇筑区左暂存点位事件
     */
    WAITING_POURING_SUB_CAR_FULL_GO_WAITING_POURING_LEFT_TEMPORARY_POSITION(10, NodeEventType.EXECUTION_EVENT),

    /**
     * 待浇筑区子车满载前往待浇筑区左暂存点位完成事件
     */
    WAITING_POURING_SUB_CAR_FULL_GO_WAITING_POURING_LEFT_TEMPORARY_POSITION_FINISHED(11, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车装载合箱体事件
     */
    SUB_CAR_LOAD_CLOSING_BOX(12, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车装载合箱体完成事件
     */
    SUB_CAR_LOAD_CLOSING_BOX_FINISHED(13, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车卸载合箱体事件
     */
    SUB_CAR_UN_LOAD_CLOSING_BOX(14, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车卸载合箱体完成事件
     */
    SUB_CAR_UN_LOAD_CLOSING_BOX_FINISHED(15, NodeEventType.COMPLETE_EVENT),

    /**
     * 待浇筑区点位母车满载前往浇筑区点位事件
     */
    WAITING_POUR_FERRY_FULL_GO_POURING_POSITION(16, NodeEventType.EXECUTION_EVENT),

    /**
     * 待浇筑区点位母车满载前往浇筑区点位完成事件
     */
    WAITING_POUR_FERRY_FULL_GO_POURING_POSITION_FINISHED(17, NodeEventType.COMPLETE_EVENT),

    /**
     * 待浇筑区子车空载前往待浇筑区左暂存点位事件
     */
    WAITING_POURING_SUB_CAR_EMPTY_GO_WAITING_POURING_LEFT_TEMPORARY_POSITION(18, NodeEventType.EXECUTION_EVENT),

    /**
     * 待浇筑区子车空载前往待浇筑区左暂存点位完成事件
     */
    WAITING_POURING_SUB_CAR_EMPTY_GO_WAITING_POURING_LEFT_TEMPORARY_POSITION_FINISHED(19, NodeEventType.COMPLETE_EVENT),

    /**
     * 待浇筑区左暂存点位子车空载前往待浇筑区点位事件
     */
    WAITING_POURING_LEFT_TEMPORARY_POSITION_SUB_CAR_EMPTY_GO_WAITING_POURIN_POSITION(20, NodeEventType.EXECUTION_EVENT),

    /**
     * 待浇筑区左暂存点位子车空载前往待浇筑区点位完成事件
     */
    WAITING_POURING_LEFT_TEMPORARY_POSITION_SUB_CAR_EMPTY_GO_WAITING_POURIN_POSITION_FINISHED(21, NodeEventType.COMPLETE_EVENT),

    /**
     * 待浇筑区点位母车空载前往上待浇筑区点位事件
     */
    WAITING_POURING_FERRY_FULL_GO_TOP_WAITING_POURING_POSITION(22, NodeEventType.EXECUTION_EVENT),

    /**
     * 待浇筑区点位母车空载前往上待浇筑区点位完成事件
     */
    WAITING_POURING_FERRY_FULL_GO_TOP_WAITING_POURING_POSITION_FINISHED(23, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区点位子车满载前往浇筑区事件
     */
    POURING_POSITION_SUB_CAR_FULL_GO_POURING(24, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区点位子车满载前往浇筑区完成事件
     */
    POURING_POSITION_SUB_CAR_FULL_GO_POURING_FINISHED(25, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区子车卸载合箱体事件
     */
    POURING_SUB_CAR_UN_LOAD_CLOSING_BOX(26, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区子车卸载合箱体完成事件
     */
    POURING_SUB_CAR_UN_LOAD_CLOSING_BOX_FINISHED(27, NodeEventType.COMPLETE_EVENT),

    /**
     * 合箱体浇筑事件
     */
    CLOSING_BOX_POURING(28, NodeEventType.EXECUTION_EVENT),

    /**
     * 合箱体浇筑完成事件
     */
    CLOSING_BOX_POURING_FINISHED(29, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区子车空载前往浇筑区轨道事件
     */
    POURING_SUB_CAR_EMPTY_GO_POURING(30, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区子车空载前往浇筑区轨道完成事件
     */
    POURING_SUB_CAR_EMPTY_GO_POURING_FINISHED(31, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区轨道母车空载前往浇筑区点位事件
     */
    POURING_FERRY_EMPTY_GO_POURING_POSITION(32, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区轨道母车空载前往浇筑区点位完成事件
     */
    POURING_FERRY_EMPTY_GO_POURING_POSITION_FINISHED(33, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区点位子车空载前往浇筑区事件
     */
    POURING_POSITION_SUB_CAR_GO_POURING(34, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区点位子车空载前往浇筑区完成事件
     */
    POURING_POSITION_SUB_CAR_GO_POURING_FINISHED(35, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区子车装载浇筑体事件
     */
    POURING_SUB_CAR_LOAD_CASTING_BODY(36, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区子车装载浇筑体完成事件
     */
    POURING_SUB_CAR_LOAD_CASTING_BODY_FINISHED(37, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区子车满载前往浇筑区点位事件
     */
    POURING_SUB_CAR_FULL_GO_POURING_POSITION(38, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区子车满载前往浇筑区点位完成事件
     */
    POURING_SUB_CAR_FULL_GO_POURING_POSITION_FINISHED(39, NodeEventType.COMPLETE_EVENT),

    /**
     * 浇筑区子车满载装载母车事件
     */
    POURING_SUB_CAR_FULL_LOAD_FERRY(40, NodeEventType.EXECUTION_EVENT),

    /**
     * 浇筑区子车满载装载母车完成事件
     */
    POURING_SUB_CAR_FULL_LOAD_FERRY_FINISHED(41, NodeEventType.COMPLETE_EVENT);

    private final Integer code;
    private final NodeEventType type;

    @Override
    public void method_x() {
    }
}
